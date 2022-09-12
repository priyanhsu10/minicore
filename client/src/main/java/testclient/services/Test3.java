package testclient.services;

public class Test3 implements ITest3 {
    private final ITestService iTestService;

    public Test3(ITestService iTestService) {
        this.iTestService = iTestService;
    }

    @Override
    public void m3() {
iTestService.getlist();
    }
}
