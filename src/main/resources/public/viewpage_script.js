/**
 * Created by david on 06/03/17.
 */
function viewPage(){}

viewPage.handler = {};

viewPage.converter = new showdown.Converter();

viewPage.handler.editClick = function() {
    //TODO: Implement
};

viewPage.handler.errorHandler = function() {
    $$("content").setHTML("<center>There was an issue receiving the page.</center>");
    $$("editbutton").hide();
};

viewPage.getContent = function() {

    var params = generalPages.getUrlContent(location.href);

    webix.ajax().get("/retrieveWikiPage?id=" + params.id, {
        error:viewPage.handler.errorHandler,
        success:function(dataString){
            if (dataString !== null) {
                var page = JSON.parse(dataString);
                var html = "<h1>" + page.title + "</h1>";
                html += "<h3>Created by " + page.author + " on " + new Date(page.creationDate) + "</h3><br/><hr/>";
                html += viewPage.converter.makeHtml(page.content);
                $$("content").setHTML(html);
            }
        }
    });

};

webix.ready(function() {
    webix.ui({
        rows:[
            generalPages.toolbarLogInSignUp,
            {
                view:"template",
                template:"<p></p>",
                id:"content",
                height:600,
                scroll:true
            },
            { },
            { cols:[
                    { },
                    { view:"button", id:"editbutton", value:"Edit Page", click:viewPage.handler.editClick },
                    { }
                ]},
            { },
            { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
        ]
    });
    viewPage.getContent();
});