function signupHandler() {}

signupHandler.strings = {};
signupHandler.strings.success = "Signup Succeeded.";
signupHandler.strings.errorNotFilled = "Signup Failed. Please fill out all fields.";
signupHandler.strings.errorPasswordMatch = "Signup Failed. Password does not match password confirm.";
signupHandler.strings.errorEmail = "Signup Failed. Invalid email.";
signupHandler.strings.errorUserName = "Signup Failed. Invalid username (only alphanumeric accepted).";
signupHandler.strings.errorName = "Signup Failed. Invalid name (only alphanumeric accepted).";
signupHandler.strings.errorLastName = "Signup Failed. Invalid last name (only alphanumeric accepted).";
signupHandler.strings.errorPassword = "Signup Failed. Invalid password (only alphanumeric accepted).";
signupHandler.strings.errorSignup = "Signup Failed.";

signupHandler.submitHandler = function( ) {

    if(signupHandler.validateSignupForm()) {
        webix.ajax().post("/signup", $$("signup_form").getValues(), {
            error:signupHandler.errorResponseHandler,
            success:signupHandler.successHandler
        });
    }

};

signupHandler.validateSignupForm = function() {

      var name = $$("name").getValue();
      var last = $$("last").getValue();
      var user = $$("user").getValue();
      var pass = $$("pass").getValue();
      var passConfirm = $$("passConfirm").getValue();
      var email = $$("email").getValue();

      // Check to make sure all of the required information is there
      if ( !name || !last || !user || !pass || !passConfirm || !email ) {
          signupHandler.errorHandler(signupHandler.strings.errorNotFilled);
      }
      // Check to make sure the password and confirm matches
      else if ( pass !== passConfirm ) {
          signupHandler.errorHandler(signupHandler.strings.errorPasswordMatch);
      }
      // Check for valid email format
      else if ( !pageUtil.validateEmail(email) ){
          signupHandler.errorHandler(signupHandler.strings.errorEmail);
      }
      // Make sure username is valid format
      else if( !pageUtil.validateAlphaNum(user) ) {
          signupHandler.errorHandler(signupHandler.strings.errorUserName);
      }
      // Make sure name is valid format
      else if( !pageUtil.validateAlphaNum(name) ) {
          signupHandler.errorHandler(signupHandler.strings.errorName);
      }
      // Make sure last name is valid format
      else if( !pageUtil.validateAlphaNum(last) ) {
          signupHandler.errorHandler(signupHandler.strings.errorLastName);
      }
      // Make sure password is valid format
      else if( !pageUtil.validateAlphaNum(pass) ) {
          signupHandler.errorHandler(signupHandler.strings.errorPassword);
      }
      else {
          return true;
      }

      return false;
};

signupHandler.successHandler = function(dataString) {
  if( dataString === null ) {
      signupHandler.errorResponseHandler();
  }
  var data = JSON.parse(dataString);
  var userName = $$("user").getValue();
  if( data !== null && ( data.userName === userName || data.email === userName ) ) {
      $$("signup_form").hide();
      $$("signup_top_label").hide();
      $$("signup_result").css = "label_text";
      $$("signup_result").setValue(signupHandler.strings.success);
      webix.alert(signupHandler.strings.success, signupHandler.signupSuccessHandler);
  } else {
      signupHandler.errorResponseHandler();
  }
};

signupHandler.errorHandler = function(message) {
    $$("signup_result").define("css","label_error");
    $$("signup_result").setValue(message);
};

signupHandler.errorResponseHandler = function(data) {
    signupHandler.errorHandler(signupHandler.strings.errorSignup);
};

signupHandler.homeHandler = function( ) {
    location.href = "/";
};

signupHandler.loginHandler = function( ) {
    location.href = "/login";
};

signupHandler.signupSuccessHandler = function( ) {
    location.href = "/";
    //location.href = "/profile";
};

signupHandler.onReady = function( ) {
  var form = [
      { view:"text", id:"user", label:"User", name:"user", labelWidth:150},
      { view:"text", id:"email", label:"Email", name:"email", labelWidth:150},
      { view:"text", id:"name", label:"First Name", name:"first", labelWidth:150},
      { view:"text", id:"last", label:"Last Name", name:"last", labelWidth:150},
      { view:"text", type:"password", id:"pass", label:"Password", name:"pass", labelWidth:150},
      { view:"text", type:"password", id:"passConfirm", label:"Confirm Password", labelWidth:150},
      { margin:10,
          cols:[
              { view:"button", value:"Signup" , type:"form", click:signupHandler.submitHandler},
              { view:"button", value:"Cancel", click:signupHandler.homeHandler }
          ]
      },
      { view:"label", align:"center", label:"", id:"signup_result" }
  ];

  webix.ui({
      rows:[
          generalPages.toolbar,
          { view:"label", id:"signup_top_label", css:"label_text", label:"Please enter your information.", align:"center"},
          { view: "form",
              cols: [
                  { },
                  {view: "form", id:"signup_form", align:"center", elements: form},
                  { }
              ]
          },
          { },
          {view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
      ]
  });

  generalPages.formatToolbar();
};

webix.ready(signupHandler.onReady);
