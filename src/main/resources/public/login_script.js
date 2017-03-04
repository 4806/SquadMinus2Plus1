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

loginHandler.loginSuccessHandler = function( ) {
    location.href = "/";
    //location.href = "/profile";
};

webix.ready(function( ) {
    var form = [
        { view:"text", id:"user", label:"User", name:"login"},
        { view:"text", type:"password", label:"Password", name:"pass"},
        { margin:10,
            cols:[
                { view:"button", value:"Login" , type:"form", click:loginHandler.submitHandler},
                { view:"button", value:"Cancel", click:generalPages.handler.homeClick }
            ]
        },
        { view:"label", align:"center", label:"", id:"login_result" }
    ];

    webix.ui({
        rows:[
            generalPages.toolbarHomeSignUp,
            { view:"label", id:"login_top_label", css:"label_text", label:"Please enter your login information.", align:"center"},
            { view: "form",
                cols: [
                    { },
                    {view: "form", id:"login_form", align:"center", elements: form},
                    { }
                ]
            },
            { },
            {view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
        ]
    });
});