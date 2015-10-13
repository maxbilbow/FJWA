package fjwa;

import click.rmx.debug.Tests;
import junit.framework.TestCase;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by bilbowm on 13/10/2015.
 */
public class PasswordHash extends TestCase {

    public void testMD5Hasg()
    {
        String password = "password";
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        String hashedPassword = passwordEncoder.encodePassword(password,null);
        Tests.note(hashedPassword);
    }

    public void testBCryptHash()
    {
        String password = "password";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        Tests.note(hashedPassword);
    }
}
