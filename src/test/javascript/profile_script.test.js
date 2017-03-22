/**
 * Created by david on 22/03/17.
 */
describe("profile_script", function(){

    profilePage.onReady();

    it("Empty User", function(){

      profilePage.handler.setContent();
      expect($$("username").isVisible()).not.toBeTruthy();
      expect($$("first").isVisible()).not.toBeTruthy();
      expect($$("last").isVisible()).not.toBeTruthy();
      expect($$("email").isVisible()).not.toBeTruthy();
      expect($$("likesheader").isVisible()).not.toBeTruthy();
      expect($$("createdheader").isVisible()).not.toBeTruthy();
      expect($$("likelist").isVisible()).not.toBeTruthy();
      expect($$("createdlist").isVisible()).not.toBeTruthy();
      expect($$("status").isVisible()).toBeTruthy();
    });

    it("Valid User With Likes and Pages", function(){
      var date = new Date();

      profilePage.handler.setContent('{"user":{"first":"test", "last":"test", "userName":"test", "email":"test@test.test"}, "likes":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "created":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}]}');
      expect($$("username").isVisible()).toBeTruthy();
      expect($$("first").isVisible()).toBeTruthy();
      expect($$("last").isVisible()).toBeTruthy();
      expect($$("email").isVisible()).toBeTruthy();
      expect($$("likesheader").isVisible()).toBeTruthy();
      expect($$("createdheader").isVisible()).toBeTruthy();
      expect($$("likelist").isVisible()).toBeTruthy();
      expect($$("createdlist").isVisible()).toBeTruthy();
      expect($$("status").isVisible()).not.toBeTruthy();
    });
});
