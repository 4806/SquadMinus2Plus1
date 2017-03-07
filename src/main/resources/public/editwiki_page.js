/**
 * Created by david on 04/03/17.
 */
function editPage(){}

editPage.handler = {};

editPage.preview = false;

editPage.converter = new showdown.Converter();

editPage.handler.acceptClick = function() {
    //TODO: Implement
};

editPage.handler.previewToggle = function() {
    if( editPage.preview === false ) {

        //If the preview page is hidden, get preview and show

        $$("rawtext").hide();
        var text = $$("rawtext").getValue();
        var html = editPage.converter.makeHtml(text);
        $$("preview").setHTML(html);
        $$("preview").show();

    } else {
        //If the preview page is showing, hide it and go to editor

        $$("preview").hide();
        $$("rawtext").show();
    }

    //Toggle the flag
    editPage.preview = editPage.preview === false;
};

webix.ready(function() {
    webix.ui({
        rows:[
            generalPages.toolbarLogInSignUp,
            { margin:10, cols:[
                {
                    view:"text",
                    id:"title",
                    placeholder:"Page Title"
                },
                {
                    view:"toggle",
                    offLabel:"Preview",
                    onLabel:"Raw View",
                    click:editPage.handler.previewToggle,
                    align:"right",
                    width:200
                }
            ]},
            {
                view:"textarea",
                id:"rawtext",
                height:600,
                scroll:true,
                placeholder:"Enter Your Wiki Content Here!"
            },
            {
                view:"template",
                template:"<p>Preview</p>",
                id:"preview",
                height:600,
                scroll:true
            },
            { },
            { margin:10,
                cols:[
                    { },
                    { view:"button", value:"Accept", click:editPage.handler.acceptClick },
                    { view:"button", value:"Cancel", click:generalPages.handler.homeClick },
                    { }
            ]},
            { },
            { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
        ]
    });

    //Don't show the preview page at start
    $$("preview").hide();
});