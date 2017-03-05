function generalPages(){}

generalPages.handler = {};

generalPages.handler.homeClick = function() {
    location.href = "/";
};

generalPages.handler.loginClick = function() {
    location.href = "/login";
};

generalPages.handler.signupClick = function() {
    location.href = "/signup";
};

generalPages.handler.searchClick = function() {
    //TODO: Implement
};

generalPages.handler.userClick = function() {
    //TODO: Implement
};

generalPages.toolbarLoggedIn = {
    view:"toolbar", elements: [
        {view:"label", label:'<img src="img/flame_white.png" width="50%"/>', width:50, align:"left"},
        {view:"label", label:"Social Wiki", align:"left"},
        {view:"search", id:"searchbox", placeholder:"Search Pages", align:"right", width:200, click:generalPages.handler.searchClick},
        {view:"button", value:"User", align:"right", width:100, click:generalPages.handler.userClick}
    ]
};

generalPages.toolbarLoggedOut = {
    view:"toolbar", container:"header", elements: [
        {view:"label", label:'<img src="img/flame_white.png" width="50%"/>', width:50, align:"left"},
        {view:"label", label:"Social Wiki", align:"left"},
        {view:"search", id:"searchbox", placeholder:"Search Pages", align:"right", width:200, click:generalPages.handler.searchClick},
        {view:"button", value:"Login", align:"right", width:100, click:generalPages.handler.loginClick},
        {view:"button", value:"Sign Up", align:"right", width:100, click:generalPages.handler.signupClick}
    ]
};

generalPages.toolbarSignup = {
    view:"toolbar", elements: [
        {view:"label", label:'<img src="img/flame_white.png" width="50%"/>', width:50, align:"left"},
        {view:"label", label:"Social Wiki", align:"left"},
        {view:"search", id:"searchbox", placeholder:"Search Pages", align:"right", width:200, click:generalPages.handler.searchClick},
        {view:"button", value:"Home", align:"right", width:100, click:generalPages.handler.homeClick},
        {view:"button", value:"Login", align:"right", width:100, click:generalPages.handler.loginClick}
    ]
};