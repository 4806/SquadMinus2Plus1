/**
 * Created by david on 02/03/17.
 */

function indexHandler(){}

indexHandler.strings = {};
indexHandler.strings.welcomeText = "Welcome to the Social Wiki!";
indexHandler.strings.aboutText = "It’s a mix of a wiki and a simple social network: a user can publish a wiki page, “like” an existing page, and “follow” another user. Editing an existing page doesn’t modify the current page; instead it creates a new page with the new content, with a “parent” link to the original page that was edited. This results in a tree of versions of pages, not unlike git. One should be able to query for all wiki pages with the same entry, and sort them using various criteria, e.g.: most “liked”, most recent, most edited, liked by the most popular user (i.e. the user who has been “followed” the most), liked by the closest user in the “follow” graph, liked by the most similar user in terms of documents they both liked. One can also lookup a user profile and see which pages they’ve created or liked, and which other users they follow.";
indexHandler.strings.aboutTheTeam = "We are a team of fourth year software engineers at Carleton University. Our team consists of: David Briglio, Chris Briglio and Connor Matthews. Together we are Group Squad Minus 2 Plus 1.";


webix.ready(function() {
    webix.ui({
        rows:[
            generalPages.toolbar,
            { height:50 },
            { view:"label", label:indexHandler.strings.welcomeText, css:"label_big", align:"center"},
            { height:50 },
            { view:"label", label:'<img src="img/flame_blue.png" height="100%"/> About The Wiki', css:"label_big", align:"center"},
            { template:"<center>"+indexHandler.strings.aboutText+"</center>", css:"label_indexNorm", align:"center", autoheight:true, borderless:true},
            { height:50 },
            { view:"label", label:'<img src="img/flame_blue.png" height="100%"/> About The Team', css:"label_big", align:"center"},
            { template:"<center>"+indexHandler.strings.aboutTheTeam+"</center>", css:"label_indexNorm", align:"center", autoheight:true, borderless:true},
            { height:50 },
            { cols:[
                { },
                { view:"button", id:"signupbutton", value:"Sign Up Now!", align:"center", click:generalPages.handler.signupClick},
                { }
            ]},
            { },
            generalPages.bottomIcon
        ]
    });

    generalPages.formatToolbar();

    if (generalPages.getCookie("user") !== null) {
      $$("signupbutton").hide();
    }
});
