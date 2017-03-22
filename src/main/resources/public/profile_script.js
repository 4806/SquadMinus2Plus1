/**
 * Created by david on 22/03/17.
 */
function profilePage(){}

profilePage.handler = {};

profilePage.profile = null;
profilePage.likes = {};
profilePage.created = {};
profilePage.strings = {};
profilePage.strings.userNamePre = "";
profilePage.strings.firstNamePre = "First: ";
profilePage.strings.lastNamePre = "Last: ";
profilePage.strings.emailNamePre = "Email: ";

profilePage.handler.error = function() {
    $$("content").setHTML("<center>There was an issue receiving the page.</center>");
    $$("editbutton").hide();
};

profilePage.handler.itemClickLikes = function(itemId) {
    var item = $$("likedlist").getItem(itemId);
    if( item.id !== undefined ) {
        location.href = "/viewpage?id=" + item.id;
    }
};

profilePage.handler.itemClickCreated = function(itemId) {
    var item = $$("createdlist").getItem(itemId);
    if( item.id !== undefined ) {
        location.href = "/viewpage?id=" + item.id;
    }
};

profilePage.handler.setContentUser = function(dataString) {
  if (dataString !== null) {
      profilePage.profile = JSON.parse(dataString);

      //User Name
      if(profilePage.profile.userName !== undefined && profilePage.profile.userName !== null) {
        $$("username").setValue(profilePage.strings.userNamePre + profilePage.profile.userName);
      }

      //Email
      if(profilePage.profile.email !== undefined && profilePage.profile.email !== null) {
        $$("email").setValue(profilePage.strings.emailNamePre + profilePage.profile.email);
      }

      //First Name
      if(profilePage.profile.first !== undefined && profilePage.profile.first !== null) {
        $$("first").setValue(profilePage.strings.firstNamePre + profilePage.profile.first);
      }

      //Last Name
      if(profilePage.profile.last !== undefined && profilePage.profile.last !== null) {
        $$("last").setValue(profilePage.strings.lastNamePre + profilePage.profile.last);
      }
  }
};

profilePage.handler.setContentLikes = function(dataString) {

};

profilePage.handler.setContentCreated = function(dataString) {

};

profilePage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    //TODO: ONLY FOR TESTING
    // profilePage.handler.setContent("{\"userName\":\"David\", \"email\":\"davidsemail@email.com\", \"first\":\"David\", \"last\":\"Briglio\"}");

    //Get user information
    // webix.ajax().get("/retrieveUser?user=" + params.user, {
    //     error:profilePage.handler.error,
    //     success:profilePage.handler.setContentUser
    // });

    //Get liked pages
    // webix.ajax().get("/retrieveUserLikes?user=" + params.user, {
    //     error:profilePage.handler.error,
    //     success:profilePage.handler.setContentLikes
    // });

    //Get pages created
    // webix.ajax().get("/retrieveUserPages?user=" + params.user, {
    //     error:profilePage.handler.error,
    //     success:profilePage.handler.setContentCreated
    // });
};

profilePage.onReady = function() {
  var toolBar = (generalPages.getCookie("user") === null) ? generalPages.toolbarLogInSignUp : generalPages.toolbarHomeUserLogOut;
  webix.ui({
      type:"clean",
      rows:[
          toolBar,
          { height:20 },
          { cols:[
            { width:30 },
            { rows:[
              { view:"label",
                id:"status",
                label:"The profile could not be loaded.",
                css:"label_error"
              },
              { view:"label",
                id:"username",
                label:"",
                css:"label_big"
              },
              { view:"label",
                id:"email",
                label:"Email: ",
                css:"label_medium"
              },
              { view:"label",
                id:"first",
                label:"First: ",
                css:"label_medium"
              },
              { view:"label",
                id:"last",
                label:"Last: ",
                css:"label_medium"
              },
            ]},
            { width:30 }
          ]},
          { height:30 },
          { cols:[
            { width:30 },
            {
                rows:[
                    {view:"template", template:"Likes", type:"header", align:"left"},
                    {
                        view:"list",
                        id:"likelist",
                        align:"center",
                        template:"#title# <div> Created By: #author# on #creationDate#</div>",
                        type:{
                            height:62
                        },
                        on:{
                            onItemClick:profilePage.handler.itemClickLikes
                        },
                        data:profilePage.likes
                    }
                ]
            },
            { width:30 },
            {
                rows:[
                    {view:"template", template:"Pages Created", type:"header", align:"left"},
                    {
                        view:"list",
                        id:"createdlist",
                        align:"center",
                        template:"#title# <div> Created By: #author# on #creationDate#</div>",
                        type:{
                            height:62
                        },
                        on:{
                            onItemClick:profilePage.handler.itemClickCreated
                        },
                        data:profilePage.created
                    }
                ]
            },
            { width:30 }
          ]},
          { height:50 },
          { view:"label", label:'<img src="img/flame_blue.png" height="50%"/>', height:100, align:"center"}
      ]
  });
  $$("likelist").clearAll();
  $$("createdlist").clearAll();
  $$("status").hide();
  profilePage.getContent();
};

webix.ready(profilePage.onReady);
