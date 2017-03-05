/**
 * Created by david on 05/03/17.
 */

function searchPage(){}

searchPage.results = [];

searchPage.handler = {};

searchPage.handler.itemClick = function(itemId) {
    var item = $$("resultlist").getItem(itemId);
    //TODO:Redirect to that page
};

searchPage.handler.error = function() {
    $$("infostatus").setValue("There was an error retrieving the results.");
    $$("resultlist").hide();
    $$("infostatus").show();
};

searchPage.searchForPages = function() {
    var searchString = generalPages.getUrlContent(location.href);
    if( !searchString.text === undefined ){
        return;
    }
    webix.ajax().get("/searchWikiPage?title=" + searchString.text, {
        error:searchPage.handler.error,
        success:function(dataString){
            if(dataString === null) {
                loginHandler.errorHandler();
            }
            var items = JSON.parse(dataString);
            $$("resultlist").clearAll();
            for( var i = 0; i < items.length; i++){
                items[i].creationDate = new Date(items[i].creationDate);
                $$("resultlist").add(items[i]);
            }
            $$("resultlist").refresh();
        }
    });
};

webix.ready(function(){
    webix.ui({
        rows:[
            generalPages.toolbarLoggedOut,
            { height:50 },
            { cols:[
                { },
                { view:"label",
                    id:"infostatus",
                    label:"",
                    align:"center",
                    css:"label_error"
                },
                {
                    view:"list",
                    id:"resultlist",
                    template:"#title# <div> Created By: #author# on #creationDate#</div>",
                    type:{
                        height:62
                    },
                    on:{
                        onItemClick:searchPage.handler.itemClick
                    },
                    data:searchPage.results
                },
                { },
            ]},
            { height:50 },
            { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
        ]
    });
    $$("infostatus").hide();
    searchPage.searchForPages();
});