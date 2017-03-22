/**
 * Created by david on 16/03/17.
 */
describe("signup_script", function(){

    signupHandler.onReady();

    beforeEach(function(){
      $$("name").setValue("test");
      $$("last").setValue("last");
      $$("user").setValue("testing");
      $$("pass").setValue("pass");
      $$("passConfirm").setValue("pass");
      $$("email").setValue("test@test.test");
    });

    describe("Invalid First Name", function(){

      it("Empty First Name", function() {
        $$("name").setValue("");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Illegal Charaters in First Name", function(){
        $$("name").setValue("&&*/&Test<");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

    });

    describe("Invalid Last Name", function(){

      it("Empty Last Name", function() {
        $$("last").setValue("");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Illegal Charaters in Last Name", function(){
        $$("last").setValue("&&*/&Test<");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

    });

    describe("Invalid User Name", function(){

      it("Empty User Name", function() {
        $$("user").setValue("");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Illegal Charaters in User Name", function(){
        $$("user").setValue("&&*/&Test<");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

    });

    describe("Invalid Password", function(){

      it("Empty Password", function() {
        $$("pass").setValue("");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Illegal Charaters in Password", function(){
        $$("pass").setValue("&&*/&Test<");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Confirmation Password does not match", function(){
        $$("passConfirm").setValue("notpass");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

    });

    describe("Invalid Email", function(){

      it("Empty Email", function() {
        $$("email").setValue("");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

      it("Illegal Email structure", function(){
        $$("email").setValue("test.test");
        expect(signupHandler.validateSignupForm()).toBe(false);

        $$("email").setValue("test@test");
        expect(signupHandler.validateSignupForm()).toBe(false);

        $$("email").setValue("test@test.");
        expect(signupHandler.validateSignupForm()).toBe(false);

        $$("email").setValue("@test.test");
        expect(signupHandler.validateSignupForm()).toBe(false);
      });

    });

    it("Valid Entries", function(){
      expect(signupHandler.validateSignupForm()).toBeTruthy();
    });

    it("Success Handling", function(){
      signupHandler.successHandler('{"userName":"testing", "email":"test@test.test"}');
      expect($$("signup_result").getValue()).toBe(signupHandler.strings.success);
    });

});
