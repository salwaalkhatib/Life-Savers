
console.log("sadada");
let express = require('express');
let dbConfig = require('./config/database');
let middlewareConfig = require('./config/middleware');
let user = require('./modules');


let app = express();

dbConfig();

middlewareConfig(app);

require('./modules');

app.use('/api',user.UserRoutes);

const PORT = 3000 || process.env.PORT;

app.listen(PORT, err => {
    if(err)
    {
        console.log(err);
    }
    else {
        console.log("TaskIt Server is Running!");
    }
});

