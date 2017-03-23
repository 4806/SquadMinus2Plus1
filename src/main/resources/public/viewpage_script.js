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

viewPage.handler.likeSuccessHandler = function() {
    $$("unlikebutton").show();
    $$("likebutton").hide();
};

viewPage.handler.likeErrorHandler = function() {
    webix.alert("There was an error with the last request, please try again");
};

viewPage.handler.likeClick = function() {
    webix.ajax().post("/likePage", "id=" + viewPage.pageData.id, {
        error: viewPage.handler.likeErrorHandler,
        success: viewPage.handler.likeSuccessHandler
    });
};

viewPage.handler.unlikeSuccessHandler = function() {
    $$("likebutton").show();
    $$("unlikebutton").hide();
};

viewPage.handler.unlikeClick = function() {
    webix.ajax().post("/unlikePage", "id=" + viewPage.pageData.id, {
        error: viewPage.handler.likeErrorHandler,
        success: viewPage.handler.unlikeSuccessHandler
    });
};

viewPage.handler.historyClick = function() {
    location.href = "/history?id=" + viewPage.pageData.id;
};

viewPage.handler.setContent = function(dataString) {
  if (dataString !== null) {
      viewPage.pageData = JSON.parse(dataString);
      var heading = "<h1>" + viewPage.pageData.title + "</h1>";
      heading += '<h3>Created by <a href="/profile?user=' + viewPage.pageData.author + '">' + viewPage.pageData.author + "</a> on " + new Date(viewPage.pageData.creationDate);

      if (viewPage.pageData.parentID === -1) { //If the original copy
        $$("previousversionbutton").hide();
      }

      heading += "</h3>";
      $$("heading").setHTML(heading);

      var content = viewPage.converter.makeHtml(viewPage.pageData.content);
      $$("content").setHTML(content);
      viewPage.setLikeButton();
  }
};

viewPage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    webix.ajax().get("/retrieveWikiPage?id=" + params.id, {
        error:viewPage.handler.errorHandler,
        success:viewPage.handler.setContent
    });
};

viewPage.setLikeButton = function() {
    var isLiked = generalPages.getCookie("isLiked");
    if (isLiked === null) {
        return;
    }
    if (isLiked === "true") {
        $$("unlikebutton").show();
        $$("likebutton").hide();
    } else {
        $$("likebutton").show();
        $$("unlikebutton").hide();
    }
    return viewPage.likeButton;
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
                                      view: "button",
                                      id: "likebutton",
                                      value: "Like",
                                      click: viewPage.handler.likeClick,
                                      autowidth: true,
                                      hidden: true
                                  },
                                  {
                                      view: "button",
                                      id: "unlikebutton",
                                      value: "Unlike",
                                      click: viewPage.handler.unlikeClick,
                                      autowidth: true,
                                      hidden: true
                                  },
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
