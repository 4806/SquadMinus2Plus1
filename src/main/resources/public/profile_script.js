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
    $$("status").show();

    //Hide all other components
    profilePage.hideAll();
};

profilePage.hideAll = function() {
  $$("username").hide();
  $$("first").hide();
  $$("last").hide();
  $$("email").hide();
  $$("likesheader").hide();
  $$("createdheader").hide();
  $$("likelist").hide();
  $$("createdlist").hide();
};

profilePage.handler.itemClickLikes = function(itemId) {
    var item = $$("likelist").getItem(itemId);
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

profilePage.handler.setContent = function(dataString) {
  if (dataString) {
      try {
        profilePage.profile = JSON.parse(dataString);
      } catch(e) {
        profilePage.handler.error();
      }

      //User Data
      if(profilePage.profile.user !== undefined && profilePage.profile.user !== null) {

        //User Name
        if(profilePage.profile.user.userName !== undefined && profilePage.profile.user.userName !== null) {
          $$("username").setValue(profilePage.strings.userNamePre + profilePage.profile.user.userName);
        }

        //Email
        if(profilePage.profile.user.email !== undefined && profilePage.profile.user.email !== null) {
          $$("email").setValue(profilePage.strings.emailNamePre + profilePage.profile.user.email);
        }

        //First Name
        if(profilePage.profile.user.first !== undefined && profilePage.profile.user.first !== null) {
          $$("first").setValue(profilePage.strings.firstNamePre + profilePage.profile.user.first);
        }

        //Last Name
        if(profilePage.profile.user.last !== undefined && profilePage.profile.user.last !== null) {
          $$("last").setValue(profilePage.strings.lastNamePre + profilePage.profile.user.last);
        }

        $$("username").show();
        $$("first").show();
        $$("last").show();
        $$("email").show();
        $$("status").hide();
      }

      //Liked Pages - should be already in the object, just refresh the table
      if( profilePage.profile.likes !== undefined && profilePage.profile.likes !== null ) {
        $$("likelist").show();
        $$("likesheader").show();
        $$("likelist").refresh();
      }

      //Created Pages - should be already in the object, just refresh the table
      if( profilePage.profile.created !== undefined && profilePage.profile.created !== null ) {
        $$("createdlist").show();
        $$("createdheader").show();
        $$("createdlist").refresh();
      }
  } else {
    profilePage.handler.error();
  }
};

profilePage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    //TODO: ONLY FOR TESTING
    // profilePage.handler.setContent("{\"userName\":\"David\", \"email\":\"davidsemail@email.com\", \"first\":\"David\", \"last\":\"Briglio\"}");

    //Get user information
    // webix.ajax().get("/retrieveUser?user=" + params.user, {
    //     error:profilePage.handler.error,
    //     success:profilePage.handler.setContent
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
                    {view:"template", id:"likesheader", template:"Likes", type:"header", align:"left"},
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
                    {view:"template", id:"createdheader", template:"Pages Created", type:"header", align:"left"},
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
  profilePage.hideAll();
  profilePage.getContent();
};

webix.ready(profilePage.onReady);
