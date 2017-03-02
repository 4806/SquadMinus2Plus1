/**
 * Created by david on 02/03/17.
 */

function indexHandler(){}

indexHandler.signupHandler = function( ) {
    location.href = "/signup";
};

indexHandler.loginHandler = function( ) {
    location.href = "/login";
};

indexHandler.searchHandler = function( ) {

};

indexHandler.strings = {};
indexHandler.strings.welcomeText = "Welcome to the Social Wiki!";
indexHandler.strings.aboutText = "It’s a mix of a wiki and a simple social network: a user can publish a wiki page, “like” an existing page, and “follow” another user. Editing an existing page doesn’t modify the current page; instead it creates a new page with the new content, with a “parent” link to the original page that was edited. This results in a tree of versions of pages, not unlike git. One should be able to query for all wiki pages with the same entry, and sort them using various criteria, e.g.: most “liked”, most recent, most edited, liked by the most popular user (i.e. the user who has been “followed” the most), liked by the closest user in the “follow” graph, liked by the most similar user in terms of documents they both liked. One can also lookup a user profile and see which pages they’ve created or liked, and which other users they follow.";

document.onreadystatechange = function() {
    webix.ui({
        rows:[
            { view:"toolbar", elements: [
                    {view:"label", label:'<img src="img/flame_white.png" width="50%"/>', width:50, align:"left"},
                    {view:"label", label:"SM2P1 Social Wiki", align:"left"},
                    {view:"text", label:"Search Pages", align:"right", labelWidth:100, width:300},
                    {view:"button", type:"icon", icon:"search", align:"right", width:30, click:indexHandler.searchHandler},
                    {view:"button", value:"Login", align:"right", width:100, click:indexHandler.loginHandler},
                    {view:"button", value:"Sign Up", align:"right", width:100, click:indexHandler.signupHandler}
                ]
            },
            {view:"label", label:indexHandler.strings.welcomeText, css:"label_big", align:"center"},
            {view:"layout",
                cols:[
                    {view:"label", label:'<img src="img/flame_blue.png" height="50%"/> About', height:100, css:"label_big", align:"center"},
                    {view:"label", label:indexHandler.strings.aboutText, css:"label_indexNorm", align:"left", labelposition:"top"}
                ]}
        ]
    });
};