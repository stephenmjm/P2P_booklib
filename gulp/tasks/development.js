var promisify   = require('promisify-me')
var express     = require('express');
var bodyParser  = require('body-parser');
var morgan      = require('morgan');
var Storage     = promisify(require('nedb'), 'nedb');
var R           = require('ramda');
var gulp        = require('gulp');
var jwt         = require("jsonwebtoken");
var config      = require('../config').server;

// init storage
db              = {}
db.accounts     = new Storage();
db.books        = new Storage();
db.tokens       = new Storage();
db.transactions = new Storage();
R.compose(
  R.forEach(function(col) {col.loadDatabase();}),
  R.values)(db);

// app server
var port  = process.env.PORT || config.development.port;
app       = express();

gulp.task('development', function() {
  app.listen(port, function() {
    console.log("app is listen on port " + port);
  });
});

// middle-wares
app.use(express.static(config.development.root));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(morgan("dev"));
app.use(function(req, res, next) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST');
  res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type, Authorization');
  next();
})

//routers

//common functions
var tokenlize = function(account) {
  var token = jwt.sign(R.pick(['email'], account), 'secret');
  return db.tokens.insert(R.compose(
    R.mixin({token: token}),
    R.pick(['email']))(account)); //return a promise
}

var ensureAuthenticated = function(req, res, next) {
  var bearerToken;
  var bearerHeader = req.headers["authorization"];
  if (typeof bearerHeader !== 'undefined') {
    var bearer = bearerHeader.split(/\s+/);
    bearerToken = bearer[1];
    req.session = jwt.decode(bearerToken);
    next();
  } else {
    res.send(403);
  }
}

// account restful endpoints

app.post('/rest/account/signup', function(req, res) {
  db.accounts.find(R.pick(['email'], req.body))
  .exec()
  .then(function(accounts) {
    if (R.isEmpty(accounts)) return db.accounts.insert(
      R.pick(['email', 'password'], req.body));
    else throw 'account is already registered';
  })
  .then(function(account) {
    return tokenlize(account);
  })
  .then(function(token) {
    res.json(token);
  })
  .fail(function(reason) {
    res.json({message: "error, " + reason});
  });
});

app.post('/rest/account/signin', function(req, res) {
  db.accounts.find(R.pick(['email', 'password'], req.body))
  .exec()
  .then(function(accounts) {
    console.log(accounts);
    if (R.isEmpty(accounts)) throw 'failed to sign in account';
    else return tokenlize(R.head(accounts));
  })
  .then(function(token) {
    res.json(token);
  })
  .fail(function(reason) {
    res.json({message: 'error ' + reason});
  });
});

// book restful endpoints

app.get('/rest/book/:id', function(req, res) {
  db.books.find({_id: req.params.id})
  .exec()
  .then(function(books) {
    if (R.isEmpty(books)) throw 'book not found';
    else res.json(R.head(books));
  })
  .fail(function(reason) {
    res.json({message: 'error' + reason});
  })
});

app.get('/rest/book', function(req, res) {
  db.books.find({})
  .exec()
  .then(function(books) {
    res.json(R.filter(
      function(book) {book !== null},
      books));
  })
  .fail(function(reason) {
    res.json({message: 'error' + reason});
  })
});

app.post('/rest/book', ensureAuthenticated, function(req, res) {
  db.books.find(R.pick(['isbn'], req.body))
  .exec()
  .then(function(books) {
    // it is ok to add same book more than one times
    return db.books.insert(R.compose(
      R.mixin({owner: req.session.email, borrower: null}),
      R.pick(['isbn', 'title', 'coverImage']))(req.body));
  })
  .then(function(book) {
    res.json(book);
  })
  .fail(function(reason) {
    res.json({message: "error, " + reason});
  });
});

app.post('/rest/book/:id/borrow', ensureAuthenticated, function(req, res) {
  var iCanBorrow = R.compose(
    R.isEmpty,
    R.filter(function(book) {book.borrower != null}));
  db.books.find({_id: req.params.id})
  .exec()
  .then(function(books) {
    if (iCanBorrow(books)) return db.books.update(
      {_id: req.params.id}, {$set: {borrower: req.session.email}});
    else 'book not found';
  })
  .then(function(book) {
    return db.transactions.insert({
      isbn: book.isbn,
      borrower: req.session.email,
      action: 'borrow',
      borrowedData: new Date()
    });
  })
  .then(function(transaction) {
    return res.json(transaction);
  })
  .fail(function(reason) {
    res.json({message: "error, " + reason});
  });
});

app.post('/rest/book/:id/return', ensureAuthenticated, function(req, res) {
  var iBorrowed = R.compose(
    R.isEmpty,
    R.filter(function(book) {book.borrower === req.session.email}));
  db.books.find({_id: req.params.id})
  .exec()
  .then(function(books) {
    if (iBorrowed(books)) return db.books.update(
      {_id: req.params.id}, {$set: {borrower: null}});
    else 'book not found';
  })
  .then(function(book) {
    return db.transactions.insert({
      isbn: book.isbn,
      borrower: req.session.email,
      action: 'return',
      returnData: new Date()
    });
  })
  .then(function(transaction) {
    return res.json(transaction);
  })
  .fail(function(reason) {
    res.json({message: "error, " + reason});
  });
});