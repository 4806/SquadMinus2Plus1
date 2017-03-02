function loginHandler() {}

loginHandler.submitHandler = function( ) {
    webix.ajax().post("/login", $$("login_form").getValues(), {
        error:loginHandler.errorHandler,
        success:function(dataString){
            if(dataString === null) {
                loginHandler.errorHandler();
            }
            var data = JSON.parse(dataString);
            var userName = $$("user").getValue();
            if( data !== null && (data.userName === userName || data.email === userName) ) {
                $$("login_form").hide();
                $$("login_top_label").hide();
                $$("login_result").css = "label_text";
                $$("login_result").setValue("Login Succeeded.");
                webix.alert("User Login Successful.", loginHandler.loginSuccessHandler);
            } else {
                loginHandler.errorHandler();
            }
        }
    });
};

loginHandler.errorHandler = function( ) {
    $$("login_result").define("css","label_error");
    $$("login_result").setValue("Login Failed. Please Try Again.");
};

loginHandler.homeHandler = function( ) {
    location.href = "/";
};

loginHandler.signupHandler = function( ) {
    location.href = "/signup";
};

loginHandler.loginSuccessHandler = function( ) {
    location.href = "/";
    //location.href = "/profile";
};