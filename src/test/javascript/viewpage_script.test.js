/**
 * Created by david on 16/03/17.
 */
describe("viewpage_script", function(){
  var date = new Date().getTime();
  viewPage.onReady();
  describe("Empty Page", function(){
    it("Page Content is null", function(){
      expect(viewPage.pageData).toBeNull();
    });
  });

  describe("Existing Page With Parent", function(){
    beforeEach(function(){
      viewPage.handler.setContent('{"id":1, "content":"#Testing!", "title":"Test", "author":"test", "creationDate":' + date + ', "parentID":0, "views":1, "likes":0}');
    });

    it("Page Content is correct", function(){
      expect(viewPage.pageData.content).toBe("#Testing!");
      expect(viewPage.pageData.title).toBe("Test");
      expect(viewPage.pageData.author).toBe("test");
      expect(viewPage.pageData.creationDate).toBe(date);
      expect(viewPage.pageData.parentID).toBe(0);
      expect(viewPage.pageData.id).toBe(1);
      expect(viewPage.pageData.views).toBe(1);
      expect(viewPage.pageData.likes).toBe(0);
    });

    it("Parent button should be showing", function(){
      expect($$("previousversionbutton").isVisible()).toBeTruthy();
    });
  });

  describe("Existing Page No Parent", function(){
    beforeEach(function(){
      viewPage.handler.setContent('{"id":1, "content":"#Testing!", "title":"Test", "author":"test", "creationDate":' + date + ', "parentID":-1, "views":1, "likes":1}');
    });

    it("Page Content is correct", function(){
      expect(viewPage.pageData.content).toBe("#Testing!");
      expect(viewPage.pageData.title).toBe("Test");
      expect(viewPage.pageData.author).toBe("test");
      expect(viewPage.pageData.creationDate).toBe(date);
      expect(viewPage.pageData.parentID).toBe(-1);
      expect(viewPage.pageData.id).toBe(1);
      expect(viewPage.pageData.views).toBe(1);
      expect(viewPage.pageData.likes).toBe(1);
    });

    it("No parent button should be showing", function(){
      expect($$("previousversionbutton").isVisible()).not.toBeTruthy();
    });
  });

});
