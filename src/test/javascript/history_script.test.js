/**
 * Created by david on 22/03/17.
 */
describe("history_script", function(){

    historyPage.onReady();

    it("Empty History Result", function(){

      historyPage.handler.success('[]');
      expect($$("resultlabel").getValue()).toBe(historyPage.strings.noHistory);
      expect($$("resultlist").isVisible()).not.toBeTruthy();
    });

    it("Valid History Result", function(){
      var date = new Date();

      historyPage.handler.success('[{"title":"Test", "creationDate":"'+ date + '", "author":"tester"},{"title":"Test", "creationDate":"'+ date + '", "author":"tester2"}]');
      expect($$("resultlabel").getValue()).toBe(historyPage.strings.pageHistory);
      expect($$("resultlist").getNextId()).not.toBeNull();
      expect($$("resultlist").isVisible()).toBeTruthy();
    });

    it("Error ", function(){
      var date = new Date();

      historyPage.handler.error();
      expect($$("resultlabel").getValue()).toBe(historyPage.strings.error);
      expect($$("resultlist").isVisible()).not.toBeTruthy();
    });
});
