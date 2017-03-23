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

generalPages.handler.logoutClick = function() {
    webix.ajax().post("/logout", {}, {
        error:function() {
            webix.alert("User Logout Unsuccessful", function() {
                    location.href = "/";
                });
            },
        success:function() {
            webix.alert("User Logout Successful", function() {
                    location.href = "/";
                });
            }
    });
};

generalPages.handler.searchClick = function() {
    location.href = "/search?text=" + $$("searchbox").getValue();
};

generalPages.handler.userClick = function() {
    location.href = "/profile?user=" + generalPages.getCookie("user");
};

generalPages.handler.addPageClick = function() {
    location.href = "/editwiki";
};

generalPages.handler.searchEnterPressed = function(key) {
  if( key === 13 ) { //Enter was pressed
    location.href = "/search?text=" + $$("searchbox").getValue();
  }
};

generalPages.getCookie = function (name) {
    var re = new RegExp(name + "=([^;]+)");
    var value = re.exec(document.cookie);
    return (value !== null) ? value[1] : null;
};

generalPages.elements = {};
generalPages.elements.toolbarFlame = { view:"label", label:'<img src="img/flame_white.png" width="50%" onclick="generalPages.handler.homeClick()" style="cursor: pointer;"/>', width:50, align:"left" };
generalPages.elements.toolbarSearch = {view:"search", id:"searchbox", placeholder:"Search Pages", align:"right", width:200, on:{onSearchIconClick:generalPages.handler.searchClick, onKeyPress:generalPages.handler.searchEnterPressed}};

generalPages.toolbarUserAdd = {
    view:"toolbar", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"User", align:"right", width:100, click:generalPages.handler.userClick}
    ]
};

generalPages.toolbarHomeSignUp = {
    view:"toolbar", container:"header", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"Sign Up", align:"right", width:100, click:generalPages.handler.signupClick}
    ]
};

generalPages.toolbarLogInSignUp = {
    view:"toolbar", container:"header", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"Login", align:"right", width:100, click:generalPages.handler.loginClick},
        {view:"button", value:"Sign Up", align:"right", width:100, click:generalPages.handler.signupClick}
    ]
};

generalPages.toolbarHomeUserLogOut = {
    view:"toolbar", container:"header", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"New Page", align:"right", width:100, click:generalPages.handler.addPageClick},
        {view:"button", value:generalPages.getCookie("user"), align:"right", width:100, click:generalPages.handler.userClick},
        {view:"button", value:"Logout", align:"right", width:100, click:generalPages.handler.logoutClick}
    ]
};

generalPages.toolbarUserLogOut = {
    view:"toolbar", container:"header", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"New Page", align:"right", width:100, click:generalPages.handler.addPageClick},
        {view:"button", value:generalPages.getCookie("user"), align:"right", width:100, click:generalPages.handler.userClick},
        {view:"button", value:"Logout", align:"right", width:100, click:generalPages.handler.logoutClick}
    ]
};

generalPages.toolbarHomeLogin = {
    view:"toolbar", elements: [
        generalPages.elements.toolbarFlame,
        {view:"label", label:"Social Wiki", align:"left"},
        generalPages.elements.toolbarSearch,
        {view:"button", value:"Login", align:"right", width:100, click:generalPages.handler.loginClick}
    ]
};
