/**
 * Created by david on 04/03/17.
 */
function editPage(){}

editPage.handler = {};

editPage.preview = false;

editPage.converter = new showdown.Converter();

editPage.pageContent = null;

// If the page was submitted successfully, make a popup and redirect to the page
editPage.handler.pageSubmittedSuccess = function(dataString) {
  var newPage = JSON.parse(dataString);
  if(newPage.id !== undefined) {
      webix.alert("Page submitted successfully!.", function(){ location.href = "/viewpage?id=" + newPage.id; });
  }
};

editPage.handler.acceptClick = function() {
    var pageData = {};
    pageData.content = $$("rawtext").getValue();
    pageData.title = $$("title").getValue();

    if(editPage.pageContent !== null) {
        pageData.parentID = editPage.pageContent.id;
    } else {
        pageData.parentID = -1;
    }

    webix.ajax().post("/createWikiPage", pageData, {
        error:function(){ webix.alert("Could not submit the page.", function(){}); },
        success:editPage.handler.pageSubmittedSuccess
    });
};

editPage.handler.errorHandler = function() {
    webix.alert("Could not retrieve the page.", function(){});
};

// Sets the page content after receiving the page
editPage.handler.setContent = function(dataString) {
    if (dataString !== null) {
        editPage.pageContent = JSON.parse(dataString);
        if(editPage.pageContent.content !== undefined) {
            $$("rawtext").setValue(pageUtil.htmlDecode(editPage.pageContent.content));
        }
        if(editPage.pageContent.title !== undefined) {
            $$("title").setValue(pageUtil.htmlDecode(editPage.pageContent.title));
        }
    }
};

editPage.getContent = function() {
    var params = pageUtil.getUrlContent(location.href);
    if(params.id !== undefined) {
        webix.ajax().get("/retrieveWikiPage?id=" + params.id, {
            error:editPage.handler.errorHandler,
            success:editPage.handler.setContent
        });
    }
};

editPage.handler.previewToggle = function() {
    if( editPage.preview === false ) {

        //If the preview page is hidden, get preview and show

        $$("rawtext").hide();
        var text = $$("rawtext").getValue();
        var escaped = $("<div>").text(text).html();
        var html = editPage.converter.makeHtml(escaped);
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

editPage.onReady = function() {
  webix.ui({
      rows:[
          generalPages.toolbar,
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
              height:650,
              scroll:"xy",
              placeholder:"Enter Your Wiki Content Here!"
          },
          {
              view:"template",
              template:"<p>Preview</p>",
              id:"preview",
              autoheight:true,
              css:"page_content"
          },
          { height:60 },
          { margin:10,
              cols:[
                  { },
                  { view:"button", value:"Accept", click:editPage.handler.acceptClick },
                  { view:"button", value:"Cancel", click:generalPages.handler.homeClick },
                  { }
          ]},
          { height:30 },
          generalPages.bottomIcon
      ]
  });

  generalPages.formatToolbar();

  //Don't show the preview page at start
  $$("preview").hide();
  editPage.getContent();
};

webix.ready(editPage.onReady);
