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
profilePage.strings.likesLabel = "Likes";
profilePage.strings.createdLabel = "Created Pages";
profilePage.strings.followingLabel = "Followed Users";
profilePage.strings.usersFollowingLabel = "Users Following";

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
  $$("createdheader").hide();
  $$("createdlist").hide();
  $$("likesheader").hide();
  $$("likelist").hide();
  $$("usersfollowing").hide();
  $$("usersfollowinglist").hide();
  $$("followedusers").hide();
  $$("followlist").hide();
  $$("followeduseractivity").hide();
  $$("followactivitylist").hide();
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
        $$("likelist").clearAll();
        for( var i = 0; i < profile.likedProxyPages.length; i++){
            profile.likedProxyPages[i].creationDate = pageUtil.getFormattedDate(profile.likedProxyPages[i].creationDate);
            $$("likelist").add(profile.likedProxyPages[i]);
        }
        $$("likesheader").setHTML(profilePage.strings.likesLabel + " (" + profile.likedProxyPages.length + ")");
        $$("likelist").refresh();
      }

      //Created Pages - should be already in the object, just refresh the table
      if( profile.createdProxyPages !== undefined && profile.createdProxyPages !== null && profile.createdProxyPages.length > 0 ) {
        $$("createdlist").clearAll();
        for( var n = 0; n < profile.createdProxyPages.length; n++){
            profile.createdProxyPages[n].creationDate = pageUtil.getFormattedDate(profile.createdProxyPages[n].creationDate);
            $$("createdlist").add(profile.createdProxyPages[n]);
        }
        $$("createdheader").setHTML(profilePage.strings.createdLabel + " (" + profile.createdProxyPages.length + ")");
        $$("createdlist").refresh();
      }

      $$("createdheader").show();
      $$("likesheader").show();
      $$("createdlist").show();
      $$("likelist").show();

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
  if (dataString) {
      try {
        users = JSON.parse(dataString);
      } catch(e) {
        return;
      }

      for( var i in users ) {
        $$("followlist").add({"userName":users[i]});
      }
      $$("followlist").show();

      if(users.length > 0) {
        $$("followedusers").setHTML(profilePage.strings.followingLabel + " (" + users.length + ")");
      } else {
        $$("followedusers").setHTML(profilePage.strings.followingLabel);
      }

      $$("followedusers").show();
      $$("followlist").refresh();
  }
};

profilePage.handler.setUsersFollowing = function(dataString) {
  if (dataString) {
      try {
        users = JSON.parse(dataString);
      } catch(e) {
        return;
      }

      for( var i in users ) {
        $$("usersfollowinglist").add({"userName":users[i]});
      }
      $$("usersfollowinglist").show();

      if(users.length > 0) {
        $$("usersfollowing").setHTML(profilePage.strings.usersFollowingLabel + " (" + users.length + ")");
      } else {
        $$("usersfollowing").setHTML(profilePage.strings.usersFollowingLabel);
      }

      $$("usersfollowing").show();
      $$("usersfollowinglist").refresh();
  }
};

// profilePage.handler.setFollowingUserActivity = function(dataString) {
//
// };

profilePage.handler.itemClickFollowedUser = function(itemId) {
  var item = $$("followlist").getItem(itemId);
  if(item !== null) {
    location.href = "/profile?user=" + item.userName;
  }
};

profilePage.handler.itemClickUserFollowing = function(itemId) {
  var item = $$("usersfollowinglist").getItem(itemId);
  if(item.userName !== undefined) {
    location.href = "/profile?user=" + item.userName;
  }
};

profilePage.getContent = function() {

    var params = pageUtil.getUrlContent(location.href);

    //Get user information + liked pages + created pages
    webix.ajax().get("/retrieveUser", {"user":params.user}, {
        error:profilePage.handler.error,
        success:profilePage.handler.setContentLikesCreated
    });

    //Get following users
    webix.ajax().post("/getFollowingUsers", {"user":params.user}, {
        error:function(){
          $$("followedusers").hide();
          $$("followlist").hide();
        },
        success:profilePage.handler.setFollowingUsers
    });

    //Get users following
    webix.ajax().post("/getUsersFollowing", {"user":params.user}, {
        error:function(){
          $$("usersfollowing").hide();
          $$("usersfollowinglist").hide();
        },
        success:profilePage.handler.setUsersFollowing
    });

    //Get following user activity
    // webix.ajax().post("/followactivity", {"user":params.user}, {
    //     error:function(){
    //       // $$("followeduseractivity").hide();
    //       // $$("followactivitylist").hide();
    //     },
    //     success:profilePage.handler.setFollowingUserActivity
    // });
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
                rows:[
                  {
                    cols:[
                      {
                          rows:[
                              {view:"template", id:"likesheader", template:profilePage.strings.likesLabel, type:"header", align:"left"},
                              {
                                  view:"list",
                                  id:"likelist",
                                  align:"center",
                                  template:"#title# <div> Created by #author# on #creationDate#</div>",
                                  height:250,
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
                              {view:"template", id:"createdheader", template:profilePage.strings.createdLabel, type:"header", align:"left"},
                              {
                                  view:"list",
                                  id:"createdlist",
                                  align:"center",
                                  template:"#title# <div> Created by #author# on #creationDate#</div>",
                                  height:250,
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
                            {view:"template", id:"followedusers", template:profilePage.strings.followingLabel, type:"header", align:"left"},
                            {
                                view:"list",
                                id:"followlist",
                                align:"center",
                                template:"#userName#",
                                height:250,
                                type:{
                                    height:62
                                },
                                on:{
                                    onItemClick:profilePage.handler.itemClickFollowedUser
                                }
                            }
                        ]
                      },
                      { width:30 },
                      {
                        rows:[
                            {view:"template", id:"usersfollowing", template:profilePage.strings.usersFollowingLabel, type:"header", align:"left"},
                            {
                                view:"list",
                                id:"usersfollowinglist",
                                align:"center",
                                template:"#userName#",
                                type:{
                                    height:62
                                },
                                on:{
                                    onItemClick:profilePage.handler.itemClickUserFollowing
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
                        maxHeight:500,
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
