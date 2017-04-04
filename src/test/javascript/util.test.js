/**
 * Created by david on 20/03/17.
 */
describe("util", function(){

    it("Validate Email", function(){
      expect(pageUtil.validateEmail("")).toBe(false);
      expect(pageUtil.validateEmail("test")).toBe(false);
      expect(pageUtil.validateEmail("test@")).toBe(false);
      expect(pageUtil.validateEmail("test@test")).toBe(false);
      expect(pageUtil.validateEmail("test@test.")).toBe(false);
      expect(pageUtil.validateEmail("@test.")).toBe(false);
      expect(pageUtil.validateEmail("@test.test")).toBe(false);
      expect(pageUtil.validateEmail("test@test.test")).toBe(true);
    });

    it("Validate Alpha Numeric", function(){
      expect(pageUtil.validateAlphaNum("*123")).toBe(false);
      expect(pageUtil.validateAlphaNum("1*123")).toBe(false);
      expect(pageUtil.validateAlphaNum("A*1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("A<1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("A'1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("A.1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("A-1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("A]1b23")).toBe(false);
      expect(pageUtil.validateAlphaNum("Ab23")).toBe(true);
    });

    it("Extract Page URL", function(){

      var output = pageUtil.getUrlContent("/testing?x=test&y=test2");
      expect(output.x).toBe("test");
      expect(output.y).toBe("test2");

      output = pageUtil.getUrlContent("/testing?xtest&y=test2");
      expect(output.x).toBe(undefined);
      expect(output.y).toBe("test2");

      output = pageUtil.getUrlContent("/testing?x");
      expect(output.x).toBe(undefined);

      output = pageUtil.getUrlContent("/testing?&y=*test2");
      expect(output.y).toBe("*test2");

      output = pageUtil.getUrlContent("/testing?x=test?y=test2");
      expect(output.x).toBe(undefined);
      expect(output.y).toBe("test2");

    });

    it("Unescape HTML", function(){
      expect(pageUtil.htmlDecode("&lt;test&gt;")).toBe("<test>");
      expect(pageUtil.htmlDecode("&lt;&apos;test&apos;&gt;")).toBe("<'test'>");
    });

});
