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
      expect($$("status").isVisible()).toBeTruthy();
    });

    it("Valid User With Likes and Pages", function(){
      var date = new Date();

      profilePage.handler.setContent('{"firstName":"test", "lastName":"test", "userName":"test", "email":"test@test.test", "likedPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}], "createdPages":[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}]}');
      expect($$("username").isVisible()).toBeTruthy();
      expect($$("first").isVisible()).toBeTruthy();
      expect($$("last").isVisible()).toBeTruthy();
      expect($$("email").isVisible()).toBeTruthy();
      expect($$("status").isVisible()).not.toBeTruthy();
    });
});
