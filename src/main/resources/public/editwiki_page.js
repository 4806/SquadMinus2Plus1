/**
 * Created by david on 04/03/17.
 */
function editPage(){}

editPage.handler = {};

editPage.handler.acceptClick = function() {
    //TODO: Implement
};

webix.ready(function() {
    webix.ui({
        rows:[
            generalPages.toolbarLogInSignUp,
            { view:"tabview",
                cells:[
                    {
                        header:"Raw",
                        body:{
                            view:"textarea",
                            height:600,
                            scroll:true,
                            placeholder:"Enter Your Wiki Content Here!"
                        }
                    },
                    {
                        header:"Preview",
                        body:{
                            template:"",
                            id:"markdownpreview"
                        }
                    }
            ]},
            { },
            { margin:10,
                cols:[
                    { },
                    { view:"button", value:"Accept", onclick:editPage.handler.acceptClick, width:300},
                    { view:"button", value:"Cancel", onclick:generalPages.handler.homeClick, width:300},
                    { }
            ]},
            { },
            { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
        ]
    });
});