let mongoose = require('mongoose');
let LocalStrategyMongoose = require('passport-local-mongoose');

const UserSchema = mongoose.Schema({
    username:
    {
        type:String,
        required:true,
        unique: true
        
    },
    fullName:
    {
        type:String,
        //required:true
    },
    password:
    {
        type:String,
        //required:true,
       
    },
    email:
    {
        type:String,
        required:true,
        unique:true
    },
    bloodType:
    {
        type:String,
        required:true
    },
   longitude:
   {
        type:Number
   },
   latitude:
   {
        type:Number
   },
   lastDateOfDonation:
   {
       type:String,
       
   },
   doner:
   {
       type:Boolean
   },
   mobileNumber:
   {
       type:Number
   },
   location:
   {
       type:String
   }
   



},{timestamps:true});

UserSchema.plugin(LocalStrategyMongoose);

module.exports = mongoose.model("User",UserSchema);