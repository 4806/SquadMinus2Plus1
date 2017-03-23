/**
 * Created by david on 21/03/17.
 */

function searchPage(){}

searchPage.results = [];

searchPage.handler = {};

searchPage.handler.itemClick = function(itemId) {
    var item = $$("resultlist").getItem(itemId);
    if( item.id !== undefined ) {
        location.href = "/viewpage?id=" + item.id;
    }
};

searchPage.handler.error = function() {
    $$("infostatus").setValue("There was an error retrieving the results.");
    $$("resultlist").hide();
    $$("infostatus").show();
};

searchPage.handler.advancedFilter = function () {

    webix.ajax().get("/searchWikiPage?" +
                    "title=" + $$("titleTextArea").getValue() +
                    "&user=" +  $$("usernameTextArea").getValue() +
                    "&content=" + $$("contentTextArea").getValue(),
        {
        error:searchPage.handler.error,
        success:searchPage.handler.success
    });
};

searchPage.searchForPages = function() {
    var searchString = pageUtil.getUrlContent(location.href);
    if( searchString.text === undefined ){
        return;
    }
    webix.ajax().get("/searchWikiPage?title=" + searchString.text, {
        error:searchPage.handler.error,
        success:searchPage.handler.success
    });
};

searchPage.handler.success = function(dataString) {
  if(dataString === null) {
      searchPage.handler.error();
  }
  var items = JSON.parse(dataString);

  if( items.length === 0 ){
      //Show no results

      $$("infostatus").setValue("No results were found.");
      $$("resultlist").hide();
      $$("infostatus").show();
  } else {
      //Repopulate list if results

      $$("infostatus").setValue("");
      $$("resultlist").show();
      $$("infostatus").hide();

      $$("resultlist").clearAll();
      for( var i = 0; i < items.length; i++){
          items[i].creationDate = pageUtil.getFormattedDate(items[i].creationDate);
          $$("resultlist").add(items[i]);
      }
      $$("resultlist").refresh();
  }
};

searchPage.onReady = function() {
  var toolBar = (generalPages.getCookie("user") === null) ? generalPages.toolbarHomeLogin : generalPages.toolbarHomeUserLogOut;
  webix.ui({
      rows:[
          toolBar,
          { height:50 },
          { cols:[
              { width:10 },
              { view:"label",
                  id:"infostatus",
                  label:"",
                  align:"center",
                  css:"label_error"
              },
              {
                  view:"list",
                  id:"resultlist",
                  align:"center",
                  template:"#title# <div> Created By: #author# on #creationDate#</div>",
                  type:{
                      height:62
                  },
                  on:{
                      onItemClick:searchPage.handler.itemClick
                  },
                  data:searchPage.results
              },
              { width:50 },
              {
                  rows:[
                      {
                          width:500,
                          height:275,
                          margin:5,
                          rows:[
                              {view:"template", template:"Advanced Filters", type:"header", align:"left"},
                              {view:"text", id:"titleTextArea", label:"Title:", height:40, inputWidth:500, align:"right", labelAlign:"right", placeholder:"Page title", tooltip:"Filters search by the pages title", labelWidth:130},
                              {view:"text", id:"usernameTextArea", label:"Username:", height:40, inputWidth:500, align:"right", labelAlign:"right", placeholder:"Authors username", tooltip:"Filters search by the pages author",labelWidth:130},
                              {view:"text", id:"contentTextArea", label:"Content:", height:40, inputWidth:500, align:"right", labelAlign:"right", placeholder:"Page content snippet", tooltip:"Filters search by the content of the page",labelWidth:130},
                              {cols:[ { }, {view:"button", id:"advancedFilter", autowidth:true, label:"Apply Filter", on:{onItemClick:searchPage.handler.advancedFilter}, tooltip:"Apply filters to search"} ]}
                          ]
                      },
                      { }
                  ]
              },
              { width:10 }
          ]},
          { height:50 },
          { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
      ]
  });
  $$("infostatus").hide();
  searchPage.searchForPages();
};

webix.ready(searchPage.onReady);
