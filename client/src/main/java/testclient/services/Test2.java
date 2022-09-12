package testclient.services;

public class Test2 implements ITest2 {
    private final ITest3 iTest3;

    public Test2(ITest3 iTest3) {
        this.iTest3 = iTest3;
    }

    @Override
    public void m1() {
        iTest3.m3();
    }
}
