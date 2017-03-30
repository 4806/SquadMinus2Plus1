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
  $$("unfollowbutton").hide();
  $$("followbutton").hide();
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

profilePage.handler.setContentLikesCreated = function(dataString) {
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

        //Showing the follow button will only work when the username is present
        profilePage.checkFollow();
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
      if( profile.likedProxyPages !== undefined && profile.likedProxyPages !== null && profile.likedProxyPages.length > 0 ) {
        $$("likelist").show();
        $$("likelist").clearAll();
        for( var i = 0; i < profile.likedProxyPages.length; i++){
            profile.likedProxyPages[i].creationDate = pageUtil.getFormattedDate(profile.likedProxyPages[i].creationDate);
            $$("likelist").add(profile.likedProxyPages[i]);
        }
        $$("likesheader").show();
        $$("likelist").refresh();
      }

      //Created Pages - should be already in the object, just refresh the table
      if( profile.createdProxyPages !== undefined && profile.createdProxyPages !== null && profile.createdProxyPages.length > 0 ) {
        $$("createdlist").show();
        $$("createdlist").clearAll();
        for( var n = 0; n < profile.createdProxyPages.length; n++){
            profile.createdProxyPages[n].creationDate = pageUtil.getFormattedDate(profile.createdProxyPages[n].creationDate);
            $$("createdlist").add(profile.createdProxyPages[n]);
        }
        $$("createdheader").show();
        $$("createdlist").refresh();
      }
  } else {
    profilePage.handler.error();
  }
};

profilePage.handler.followButtonClick = function() {
  webix.ajax().post("/followUser", {"user":$$("username").getValue()} , {
      error:profilePage.handler.followerror,
      success:profilePage.setFollowButton
  });
};

profilePage.handler.unfollowerror = function() {
  webix.alert("There was a problem unfollowing user.");
};

profilePage.handler.followerror = function() {
  webix.alert("There was a problem following user.");
};

profilePage.handler.unfollowButtonClick = function() {
  webix.ajax().post("/unfollowUser", {"user":$$("username").getValue()}, {
      error:profilePage.handler.unfollowerror,
      success:profilePage.setFollowButton
  });
};

profilePage.setFollowButton = function() {
  if(generalPages.getCookie("isFollowed").toLowerCase() === "true") {
    $$("followbutton").hide();
    $$("unfollowbutton").show();
  } else {
    $$("unfollowbutton").hide();
    $$("followbutton").show();
  }
};

profilePage.checkFollow = function() {
  if(generalPages.getCookie("user") !== null && generalPages.getCookie("user") !== $$("username").getValue()) {
    profilePage.setFollowButton();
  }
};

profilePage.handler.setFollowingUsers = function(dataString) {

};

profilePage.handler.setUsersFollowing = function(dataString) {

};

profilePage.handler.setFollowingUserActivity = function(dataString) {

};

profilePage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    //Get user information + liked pages + created pages
    webix.ajax().get("/retrieveUser?user=" + params.user, {
        error:profilePage.handler.error,
        success:profilePage.handler.setContentLikesCreated
    });

    //Get following users
    webix.ajax().get("/following?user=" + params.user, {
        error:function(){
          $$("followedusers").hide();
          $$("followlist").hide();
        },
        success:profilePage.handler.setFollowingUsers
    });

    //Get users following
    webix.ajax().get("/usersfollowing?user=" + params.user, {
        error:function(){
          $$("usersfollowing").hide();
          $$("usersfollowinglist").hide();
        },
        success:profilePage.handler.setUsersFollowing
    });

    //Get following user activity
    webix.ajax().get("/followactivity?user=" + params.user, {
        error:function(){
          $$("followeduseractivity").hide();
          $$("followactivitylist").hide();
        },
        success:profilePage.handler.setFollowingUserActivity
    });
};

profilePage.onReady = function() {
  webix.ui({
      type:"clean",
      rows:[
          generalPages.toolbar,
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
              {
                view:"button",
                id:"followbutton",
                value:"Follow",
                inputWidth:150,
                click:profilePage.handler.followButtonClick
              },
              {
                view:"button",
                id:"unfollowbutton",
                value:"Unfollow",
                inputWidth:150,
                click:profilePage.handler.unfollowButtonClick
              },
            ]},
            { width:30 }
          ]},
          { height:30 },
          { cols:[
              { width:30 },
              {
                autoheight:true,
                rows:[
                  {
                    cols:[
                      {
                          rows:[
                              {view:"template", id:"likesheader", template:"Likes", type:"header", align:"left"},
                              {
                                  view:"list",
                                  id:"likelist",
                                  align:"center",
                                  template:"#title# <div> Created by #author# on #creationDate#</div>",
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
                                  template:"#title# <div> Created by #author# on #creationDate#</div>",
                                  type:{
                                      height:62
                                  },
                                  on:{
                                      onItemClick:profilePage.handler.itemClickCreated
                                  }
                              }
                          ]
                      }
                    ]
                  },
                  { height:30 },
                  {
                    cols:[
                      {
                        rows:[
                            {view:"template", id:"followedusers", template:"Followed Users", type:"header", align:"left"},
                            {
                                view:"list",
                                id:"followlist",
                                align:"center",
                                template:"#userName#",
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
                            {view:"template", id:"usersfollowing", template:"Users Following", type:"header", align:"left"},
                            {
                                view:"list",
                                id:"usersfollowinglist",
                                align:"center",
                                template:"#userName#",
                                type:{
                                    height:62
                                },
                                on:{
                                    onItemClick:profilePage.handler.itemClickCreated
                                }
                            }
                        ]
                      },
                    ]
                  }
                ]
              },
              { width:30 },
              {
                autoheight:true,
                rows:[
                    {view:"template", id:"followeduseractivity", template:"Followed User Activity", type:"header", align:"left"},
                    {
                        view:"list",
                        id:"followactivitylist",
                        align:"center",
                        template:"#title# <div> Created by #author# on #creationDate#</div>",
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
            ]
          },
          { height:50 },
          generalPages.bottomIcon
      ]
  });

  generalPages.formatToolbar();
  $$("likelist").clearAll();
  $$("createdlist").clearAll();
  $$("status").hide();
  profilePage.hideAll();
  profilePage.getContent();
  $$("followbutton").hide();
  $$("unfollowbutton").hide();
};

webix.ready(profilePage.onReady);
