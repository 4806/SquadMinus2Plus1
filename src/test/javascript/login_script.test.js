/**
 * Created by david on 16/03/17.
 */
describe("login_script", function(){

    loginHandler.onReady();

    beforeEach(function(){
      $$("user").setValue("test");
    });

    // Testing the proper user being returned
    it("Successful login", function(){
      loginHandler.successHandler('{"userName":"test", "email":"test@test.test"}');
      expect($$("login_result").getValue()).toBe(loginHandler.strings.success);
    });

    // Testing a response of a different user + a null response
    it("Invalid login", function(){
      loginHandler.successHandler('{"userName":"test_error", "email":"test@test.test"}');
      expect($$("login_result").getValue()).toBe(loginHandler.strings.error);

      loginHandler.successHandler(null);
      expect($$("login_result").getValue()).toBe(loginHandler.strings.error);
    });

    // Testing an error in response
    it("Unsuccessful login", function(){
      loginHandler.errorHandler();
      expect($$("login_result").getValue()).toBe(loginHandler.strings.error);
    });
});
