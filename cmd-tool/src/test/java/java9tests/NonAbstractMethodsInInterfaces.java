package java9tests;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created by bilbowm on 29/10/2015.
 */

public class NonAbstractMethodsInInterfaces {

    interface I1 {
        default void m() { System.err.println("I1.m"); }
    }

    interface I2 {
        default void m() { System.err.println("I2.m"); }
    }

    class C implements I1, I2 {
        public void m() { I2.super.m(); System.err.println("C.m"); }
    }

    @Test
    public void mainTest() throws Throwable {
        C c = new C();
        MethodHandles.Lookup l = MethodHandles.lookup();
        MethodType t = MethodType.methodType(void.class);
        // This lookup will fail with an IllegalAccessException.
        MethodHandle di1m = l.findSpecial(I1.class, "m", t, C.class);
        di1m.invoke(c);
    }

    @Test
    @Ignore //unix only
    public void pidTest() throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(new String[]{ "/bin/sh", "-c", "echo $PPID" });

        if (proc.waitFor() == 0)
        {
            InputStream in = proc.getInputStream();
            int available = in.available();
            byte[] outputBytes = new byte[available];

            in.read(outputBytes);
            String pid = new String(outputBytes);

            System.out.println("Your pid is " + pid);
        }
    }

    @Test
    @Ignore
    public void CurrencyTest() {
//        // getting CurrencyUnits by currency code
//        CurrencyUnit euro = MonetaryCurrencies.getCurrency("EUR");
//        CurrencyUnit usDollar = MonetaryCurrencies.getCurrency("USD");
//
//        // getting CurrencyUnits by locale
//        CurrencyUnit yen = MonetaryCurrencies.getCurrency(Locale.JAPAN);
//        CurrencyUnit canadianDollar = MonetaryCurrencies.getCurrency(Locale.CANADA);
//
//        // get MonetaryAmount from CurrencyUnit
//        CurrencyUnit euro = MonetaryCurrencies.getCurrency("EUR");
//        MonetaryAmount fiveEuro = Money.of(5, euro);
//
//        // get MonetaryAmount from currency code
//        MonetaryAmount tenUsDollar = Money.of(10, "USD");
//
//        // FastMoney is an alternative MonetaryAmount factory that focuses on performance
//        MonetaryAmount sevenEuro = FastMoney.of(7, euro);
    }
}
