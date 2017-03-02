/**
 * Created by david on 02/03/17.
 */

function indexHandler(){}

indexHandler.signupHandler = function( ) {
    location.href = "/signup";
};

indexHandler.loginSuccessHandler = function( ) {
    location.href = "/login";
};

indexHandler.searchHandler = function( ) {

};

document.onreadystatechange = function() {
    webix.ui({
        rows:[
            { view:"toolbar", elements: [
                    {view:"label", label:"SM2P1 Social Wiki"},
                    {view:"text", label:"Search Pages", align:"right", labelWidth:100, width:300},
                    {view:"button", type:"icon", icon:"search", align:"right", width:30, click:indexHandler.searchHandler},
                    {view:"button", value:"Login", align:"right", width:100, click:indexHandler.loginHandler},
                    {view:"button", value:"Sign Up", align:"right", width:100, click:indexHandler.signupHandler}
                ]
            }
        ]
    });
};