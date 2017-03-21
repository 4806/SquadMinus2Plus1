/**
 * Created by david on 16/03/17.
 */
describe("search_script", function(){

    searchPage.onReady();

    // Testing searching for pages
    it("Empty Search Result", function(){

      searchPage.handler.success('[]');
      expect($$("infostatus").isVisible()).toBeTruthy();
      expect($$("resultlist").isVisible()).not.toBeTruthy();
      expect($$("infostatus").getValue()).not.toBeNull();
    });

    it("Valid Results", function(){
      var date = new Date();

      searchPage.handler.success('[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"}]');
      expect($$("resultlist").isVisible()).toBeTruthy();
      expect($$("infostatus").isVisible()).not.toBeTruthy();
      expect($$("resultlist").getNextId()).not.toBeNull();
    });
});
