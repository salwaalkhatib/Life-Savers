let mongoose = require('mongoose');

let setUpDatabase = ()=>
{
    mongoose.connect('mongodb://localhost/lifeSavers',(err)=>{
        if(err)
        {
            console.log("Error occured while setting up the database!");
        } else
        {
            console.log("MongoDb running!");
        }
    });

}

module.exports = setUpDatabase;