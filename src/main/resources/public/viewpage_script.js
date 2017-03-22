/**
 * Created by david on 06/03/17.
 */
function viewPage(){}

viewPage.handler = {};

viewPage.converter = new showdown.Converter();

viewPage.pageData = null;

viewPage.handler.editClick = function() {
    location.href = "/editwiki?id=" + viewPage.pageData.id;
};

viewPage.handler.errorHandler = function() {
    $$("content").setHTML("<center>There was an issue receiving the page.</center>");
    $$("editbutton").hide();
};

viewPage.handler.previousVersionClick = function() {
    location.href = "?id=" + viewPage.pageData.parentID;
};

viewPage.handler.historyClick = function() {
    location.href = "/history?id=" + viewPage.pageData.id;
};

viewPage.handler.setContent = function(dataString) {
  if (dataString !== null) {
      viewPage.pageData = JSON.parse(dataString);
      var heading = "<h1>" + viewPage.pageData.title + "</h1>";
      heading += "<h3>Created by " + viewPage.pageData.author + " on " + new Date(viewPage.pageData.creationDate);

      if (viewPage.pageData.parentID === -1) { //If the original copy
        $$("previousversionbutton").hide();
        $$("historybutton").hide();
      }

      heading += "</h3>"; //<br/><hr/>
      $$("heading").setHTML(heading);

      var content = viewPage.converter.makeHtml(viewPage.pageData.content);
      $$("content").setHTML(content);
  }
};

viewPage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    webix.ajax().get("/retrieveWikiPage?id=" + params.id, {
        error:viewPage.handler.errorHandler,
        success:viewPage.handler.setContent
    });
};

viewPage.onReady = function() {
  var toolBar = (generalPages.getCookie("user") === null) ? generalPages.toolbarLogInSignUp : generalPages.toolbarHomeUserLogOut;
  webix.ui({
      type:"clean",
      rows:[
          toolBar,
          {
              autoheight:true,
              type:"clean",
              cols:[
                  {
                      view: "template",
                      template: "<p></p>",
                      id: "heading",
                      autoheight:true,
                      autowidth:true
                  },
                  {
                      paddingY:20,
                      paddingX:10,
                      align:"right",
                      rows:[
                          { },
                          {
                              cols:[
                                  {
                                      view:"button",
                                      id:"editbutton",
                                      value:"Edit Page",
                                      click:viewPage.handler.editClick,
                                      autowidth:true
                                  },
                                  {
                                      view:"button",
                                      id:"previousversionbutton",
                                      value:"Previous Version",
                                      click:viewPage.handler.previousVersionClick,
                                      autowidth:true
                                  },
                                  {
                                      view:"button",
                                      id:"historybutton",
                                      value:"History",
                                      click:viewPage.handler.historyClick,
                                      autowidth:true
                                  }
                              ]
                          }
                      ]
                  }
              ]
          },
          {
              view: "template",
              template: "<p></p>",
              id: "content"
          },
          { },
          { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
      ]
  });
  viewPage.getContent();
};

webix.ready(viewPage.onReady);
