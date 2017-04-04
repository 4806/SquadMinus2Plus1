/**
 * Created by david on 22/03/17.
 */
describe("profile_script", function(){
    var date = new Date();

    beforeEach(function(){
      profilePage.onReady();
    });


    it("Empty User", function(){
      profilePage.handler.setContentLikesCreated();
      expect($$("username").isVisible()).not.toBeTruthy();
      expect($$("first").isVisible()).not.toBeTruthy();
      expect($$("last").isVisible()).not.toBeTruthy();
      expect($$("email").isVisible()).not.toBeTruthy();
      expect($$("status").isVisible()).toBeTruthy();
      expect($$("createdheader").isVisible()).not.toBeTruthy();
      expect($$("createdlist").isVisible()).not.toBeTruthy();
      expect($$("followedusers").isVisible()).not.toBeTruthy();
      expect($$("followlist").isVisible()).not.toBeTruthy();
      expect($$("usersfollowing").isVisible()).not.toBeTruthy();
      expect($$("likesheader").isVisible()).not.toBeTruthy();
      expect($$("likelist").isVisible()).not.toBeTruthy();
      expect($$("deleteuserbutton").isVisible()).not.toBeTruthy();
    });

    describe("Valid User With Likes, Pages, Followers, and Following Users", function(){

      it("User information is visible", function(){
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');
        profilePage.handler.setUsersFollowing('["test1", "test2"]');
        profilePage.handler.setFollowingUsers('["test3", "test4"]');

        expect($$("username").isVisible()).toBeTruthy();
        expect($$("first").isVisible()).toBeTruthy();
        expect($$("last").isVisible()).toBeTruthy();
        expect($$("email").isVisible()).toBeTruthy();
        expect($$("status").isVisible()).not.toBeTruthy();
        expect($$("createdheader").isVisible()).toBeTruthy();
        expect($$("createdlist").isVisible()).toBeTruthy();
        expect($$("likesheader").isVisible()).toBeTruthy();
        expect($$("likelist").isVisible()).toBeTruthy();

        expect($$("followedusers").isVisible()).toBeTruthy();
        expect($$("followlist").isVisible()).toBeTruthy();
        expect($$("usersfollowing").isVisible()).toBeTruthy();
        expect($$("usersfollowinglist").isVisible()).toBeTruthy();
        expect($$("deleteuserbutton").isVisible()).not.toBeTruthy();
      });

      it("Valid Liked Pages Data", function(){
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');

        var element0 = $$("likelist").getItem($$("likelist").getIdByIndex(0));
        expect(element0.title).toBe("Test");
        expect(element0.author).toBe("tester");
        // expect(element0.creationDate).toBe(date);
      });

      it("Valid Created Pages Data", function(){
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');

        var element0 = $$("createdlist").getItem($$("createdlist").getIdByIndex(0));
        expect(element0.title).toBe("Test2");
        expect(element0.author).toBe("tester2");
        // expect(element0.creationDate).toBe(date);
      });

      it("Valid Users Following Data", function(){
        profilePage.handler.setUsersFollowing('["test1", "test2"]');

        var element0 = $$("usersfollowinglist").getItem($$("usersfollowinglist").getIdByIndex(0));
        var element1 = $$("usersfollowinglist").getItem($$("usersfollowinglist").getIdByIndex(1));
        expect(element0.userName).toBe("test1");
        expect(element1.userName).toBe("test2");
      });

      it("Valid Following Users Data", function(){
        profilePage.handler.setFollowingUsers('["test1", "test2"]');

        var element0 = $$("followlist").getItem($$("followlist").getIdByIndex(0));
        var element1 = $$("followlist").getItem($$("followlist").getIdByIndex(1));
        expect(element0.userName).toBe("test1");
        expect(element1.userName).toBe("test2");
      });
    });

    describe("Follow Button", function(){

      it("User is not logged in", function(){
        document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        profilePage.checkFollow();
        profilePage.getContent();

        //We expect no button to be showing for follow/unfollow
        expect($$("followbutton").isVisible()).not.toBeTruthy();
        expect($$("unfollowbutton").isVisible()).not.toBeTruthy();
        expect($$("deleteuserbutton").isVisible()).not.toBeTruthy();
      });

      it("User logged in, profile is same user", function(){
        document.cookie = "user=test; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');

        //We expect no button to be showing for follow/unfollow
        expect($$("followbutton").isVisible()).not.toBeTruthy();
        expect($$("unfollowbutton").isVisible()).not.toBeTruthy();

        //We expect the delete user button to be showing
        expect($$("deleteuserbutton").isVisible()).toBeTruthy();
      });

      it("User logged in, already following", function(){
        document.cookie = "user=test2; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
        document.cookie = "isFollowed=true; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');

        //We expect there to be an unfollow button showing
        expect($$("followbutton").isVisible()).not.toBeTruthy();
        expect($$("unfollowbutton").isVisible()).toBeTruthy();

        //We expect the delete user button to not be showing
        expect($$("deleteuserbutton").isVisible()).not.toBeTruthy();
      });

      it("User logged in, not following", function(){
        document.cookie = "user=test2; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
        document.cookie = "isFollowed=false; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
        profilePage.handler.setContentLikesCreated('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedProxyPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdProxyPages":[{"title":"Test2", "creationDate":"'+ date + '", "author":"tester2"}]}');

        //We expect there to be a follow button showing
        expect($$("followbutton").isVisible()).toBeTruthy();
        expect($$("unfollowbutton").isVisible()).not.toBeTruthy();

        //We expect the delete user button to not be showing
        expect($$("deleteuserbutton").isVisible()).not.toBeTruthy();
      });

    });

});
