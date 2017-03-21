/**
 * Created by david on 16/03/17.
 */
describe("editwiki_page", function(){

    editPage.onReady();

    describe("New Page", function() {
      // Empty page
      it("Title is empty", function(){
        expect($$("title").getValue()).toBe("");
      });

      it("Raw Text is empty", function(){
        expect($$("rawtext").getValue()).toBe("");
      });

      it("Page Content is null (no parent)", function(){
        expect(editPage.pageContent).toBeNull();
      });
    });

    describe("Editing Page", function() {
      // Editing Existing Wiki
      beforeEach(function(){
        editPage.handler.setContent('{"content":"#Testing!", "title":"Test", "id":1, "parentID":0}');
      });

      it("Title is not empty", function(){
        expect($$("title").getValue()).toBe("Test");
      });

      it("Raw Text is not empty", function(){
        expect($$("rawtext").getValue()).toBe("#Testing!");
      });

      it("Page Content is not null", function(){
        expect(editPage.pageContent).not.toBeNull();
      });

      it("Page has parent", function(){
        expect(editPage.pageContent.parentID).toBe(0);
      });
    });

    describe("General activities", function() {

      it("Preview Button Click", function(){
        editPage.handler.previewToggle();
        expect($$("preview").isVisible()).toBeTruthy();
        expect($$("rawtext").isVisible()).not.toBeTruthy();

        editPage.handler.previewToggle();
        expect($$("rawtext").isVisible()).toBeTruthy();
        expect($$("preview").isVisible()).not.toBeTruthy();
      });

    });
});
