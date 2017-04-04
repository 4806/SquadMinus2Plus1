describe("General Page", function(){

  beforeEach(function(){
    document.cookie = "user=test; expires=Thu, 01 Jan 2970 00:00:00 UTC; path=/;";
    generalPages.formatToolbar();
  });

  afterEach(function(){
    //Remove the cookie for the next set of tests
    document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  });

  it("Set Empty Notification List", function(){
    generalPages.setNotifications('[]');

    var id0 = $$("notificationList").getIdByIndex(0);
    expect($$("notificationList").getItem(id0).content).toBe(generalPages.strings.emptyNotificationList);
    expect($$("notificationList").getIdByIndex(1)).toBeUndefined();

  });

  it("Set Notification List", function(){
    generalPages.setNotifications('["test1","test2"]');

    var id0 = $$("notificationList").getIdByIndex(0);
    var id1 = $$("notificationList").getIdByIndex(1);
    expect($$("notificationList").getItem(id0).content).toBe("test1");
    expect($$("notificationList").getItem(id1).content).toBe("test2");
  });

  it("Remove Notifications", function(){
    generalPages.setNotifications('["test1","test2"]');
    generalPages.clearUiNotifications();
    
    var id0 = $$("notificationList").getIdByIndex(0);
    expect($$("notificationList").getItem(id0).content).toBe(generalPages.strings.emptyNotificationList);
    expect($$("notificationList").getIdByIndex(1)).toBeUndefined();
  });


});
