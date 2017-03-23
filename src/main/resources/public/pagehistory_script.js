/**
 * Created by david on 21/03/17.
 */

function historyPage(){}

historyPage.results = [];

historyPage.strings = {};
historyPage.strings.pageHistory = "Page History";
historyPage.strings.noHistory = "No history found.";
historyPage.strings.error = "There was an error retrieving the history.";

historyPage.handler = {};

historyPage.handler.itemClick = function(itemId) {
    var item = $$("resultlist").getItem(itemId);
    if( item.id !== undefined ) {
        location.href = "/viewpage?id=" + item.id;
    }
};

historyPage.handler.error = function() {
    $$("resultlabel").setValue(historyPage.strings.error);
    $$("resultlabel").define("css","label_error");
    $$("resultlist").hide();
};

historyPage.getHistory = function() {
    var idString = pageUtil.getUrlContent(location.href);
    if( idString.id === undefined ){
        return;
    }
    webix.ajax().get("/retrieveWikiPageHistory?id=" + idString.id, {
        error:historyPage.handler.error,
        success:historyPage.handler.success
    });
};

historyPage.handler.success = function(dataString) {
  if(dataString === null) {
      historyPage.handler.error();
  }
  var items = JSON.parse(dataString);

  if( items.length === 0 ){
      //Show no results

      $$("resultlabel").setValue(historyPage.strings.noHistory);
      $$("resultlabel").define("css","label_error");
      $$("resultlist").hide();
  } else {
      //Repopulate list if results

      $$("resultlabel").setValue(historyPage.strings.pageHistory);
      $$("resultlabel").define("css","label_medium");
      $$("resultlist").show();

      $$("resultlist").clearAll();
      for( var i = 0; i < items.length; i++){
          items[i].creationDate = pageUtil.getFormattedDate(items[i].creationDate);
          $$("resultlist").add(items[i]);
      }
      $$("resultlist").refresh();
  }
};

historyPage.onReady = function() {
  var toolBar = (generalPages.getCookie("user") === null) ? generalPages.toolbarHomeLogin : generalPages.toolbarHomeUserLogOut;
  webix.ui({
      rows:[
          toolBar,
          { height:10 },
          { view:"label",
            id:"resultlabel",
            label:"Page History:",
            align:"center",
            css:"label_medium"
          },
          { height:10 },
          { cols:[
              { },
              {
                  view:"list",
                  id:"resultlist",
                  align:"center",
                  template:"#title# <div> Created By: #author# on #creationDate#</div>",
                  type:{
                      height:62
                  },
                  on:{
                      onItemClick:historyPage.handler.itemClick
                  },
                  data:historyPage.results
              },
              { }
          ]},
          { height:50 },
          { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
      ]
  });
  historyPage.getHistory();
};

webix.ready(historyPage.onReady);
