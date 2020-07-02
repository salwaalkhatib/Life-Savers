

let bodyParser = require('body-parser');
let localStrategy = require("passport-local");
let passport = require("passport");
let expressSession = require('express-session');
let User = require('../modules/users/models');

let setUpMiddleware = (app)=>
{
    app.use(bodyParser.urlencoded({extended:false}));

    app.use(expressSession({
        secret:"Wafic is the best developer!",
        resave:false,
        saveUninitialized:false
    }));

    app.use(passport.initialize());
    app.use(passport.session());

    passport.use(new localStrategy(User.authenticate()));
    passport.serializeUser(User.serializeUser());
    passport.deserializeUser(User.deserializeUser());
}

module.exports = setUpMiddleware;