/**
 * Created by david on 22/03/17.
 */
function profilePage(){}

profilePage.handler = {};

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
      var profile = {};
      try {
        profile = JSON.parse(dataString);
      } catch(e) {
        profilePage.handler.error();
      }

      //User Data

      //User Name
      if(profile.userName !== undefined && profile.userName !== null) {
        $$("username").setValue(profilePage.strings.userNamePre + profile.userName);
        $$("username").show();
      }

      //Email
      if(profile.email !== undefined && profile.email !== null) {
        $$("email").setValue(profilePage.strings.emailNamePre + profile.email);
        $$("email").show();
      }

      //First Name
      if(profile.firstName !== undefined && profile.firstName !== null) {
        $$("first").setValue(profilePage.strings.firstNamePre + profile.firstName);
        $$("first").show();
      }

      //Last Name
      if(profile.lastName !== undefined && profile.lastName !== null) {
        $$("last").setValue(profilePage.strings.lastNamePre + profile.lastName);
        $$("last").show();
      }

      $$("status").hide();

      //Liked Pages - should be already in the object, just refresh the table
      if( profile.likedPages !== undefined && profile.likedPages !== null && profile.likedPages.length > 0 ) {
        $$("likelist").show();
        $$("likelist").clearAll();
        for( var i = 0; i < profile.likedPages.length; i++){
            profile.likedPages[i].creationDate = new Date(profile.likedPages[i].creationDate);
            $$("likelist").add(profile.likedPages[i]);
        }
        $$("likesheader").show();
        $$("likelist").refresh();
      }

      //Created Pages - should be already in the object, just refresh the table
      if( profile.createdPages !== undefined && profile.createdPages !== null && profile.createdPages.length > 0 ) {
        $$("createdlist").show();
        $$("createdlist").clearAll();
        for( var n = 0; n < profile.createdPages.length; n++){
            profile.createdPages[n].creationDate = new Date(profile.createdPages[n].creationDate);
            $$("likelist").add(profile.createdPages[n]);
        }
        $$("createdheader").show();
        $$("createdlist").refresh();
      }
  } else {
    profilePage.handler.error();
  }
};

profilePage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    //Get user information
    webix.ajax().get("/retrieveUser?user=" + params.user, {
        error:profilePage.handler.error,
        success:profilePage.handler.setContent
    });
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
                        template:"#title# <div> Created on #creationDate#</div>",
                        type:{
                            height:62
                        },
                        on:{
                            onItemClick:profilePage.handler.itemClickLikes
                        }
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
                        template:"#title# <div> Created on #creationDate#</div>",
                        type:{
                            height:62
                        },
                        on:{
                            onItemClick:profilePage.handler.itemClickCreated
                        }
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
