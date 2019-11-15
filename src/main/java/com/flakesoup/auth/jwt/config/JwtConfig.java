package com.flakesoup.auth.jwt.config;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import java.util.HashMap;

@Configuration
public class JwtConfig {

  /** RSA私钥 */
  private static final String PRIVATE_KEY =
      "-----BEGIN RSA PRIVATE KEY-----\n"
          + "MIICXQIBAAKBgQDibVFda5MKAFkQL07iBlN43GbnJE7yAbc+ShdPRocgKCp3Sxu3\n"
          + "Hc8aebzugDV+Dh6URlYZj5bPyGt2en/eP2YpAW96Kblg5nyxxWmAI6cVWs9FizA8\n"
          + "SRk3h0kThomssBWRUsjzWUAmTYucY/8hjvgxzLPUUX5UtaxCna8I5OHY8wIDAQAB\n"
          + "AoGBAL9xNFErajgTkToY9bYvKRZQK4UU8ta1UqyM0maJuCgdLcKNM5LA1mGJOo/g\n"
          + "wNmisIInchbMi/OEfi+/ZSuRKRq9OPfMcLID3x4/9mRlK31e90YYegiNMKz7RD7T\n"
          + "w0ZsbNemSAwaHDjx+dZFSYCIE9EEyJsKSMePLfmL90gfA7HZAkEA9/k+xYFIbMgA\n"
          + "Kqm3FhxZjzPF3zJlqTGyHBLyIrEUfc2GHcsLa7LT1eARClcGlXDDMHmv74Nkp9mN\n"
          + "93tKt4RlbwJBAOnBiJvPPypcaMKfISBa7X7UNF8LZLUts/+EiCFI8q0hlO3kHq8H\n"
          + "HWkEMmCVR5U9mVx1bVL612bZMYT5yMiNar0CQQCMCBtziyNsErFNZlO2z8GfhZwb\n"
          + "A6m3FxI+mlBUWO16cWJoVq4XXoATyhm1XhmgsHH5YO6Ccg+YXdm2xNAXvFNPAkBn\n"
          + "bB1I8pT75Q7krQs3CYPyjWjudFgGYUY2Uyj3sRLNzwHZjwiUYA1/HUA8w098lFh6\n"
          + "M+o+wIT1GDt0nh9bvFXxAkAg6reUGTclTRXc9JTpXYIltVEne+YKegK6RqeEJUxC\n"
          + "tDB39vwX1Ac/aMtizoeV+RHpuxdKol+7DcNoaHafuGpi\n"
          + "-----END RSA PRIVATE KEY-----";

  /** RSA公钥 */
  private static final String PUBLIC_KEY =
      "-----BEGIN PUBLIC KEY-----\n"
          + "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDibVFda5MKAFkQL07iBlN43Gbn\n"
          + "JE7yAbc+ShdPRocgKCp3Sxu3Hc8aebzugDV+Dh6URlYZj5bPyGt2en/eP2YpAW96\n"
          + "Kblg5nyxxWmAI6cVWs9FizA8SRk3h0kThomssBWRUsjzWUAmTYucY/8hjvgxzLPU\n"
          + "UX5UtaxCna8I5OHY8wIDAQAB\n"
          + "-----END PUBLIC KEY-----";

  /** 使用私钥签名 */
  @Bean
  public RsaSigner jwtSigner() {
    return new RsaSigner(PRIVATE_KEY);
  }

  /** 使用公钥验签(这里是可以通过私钥生成密钥对,包含公钥) */
  @Bean
  public RsaVerifier jwtVerifier() {
    return new RsaVerifier(PUBLIC_KEY);
  }

  public static void main(String[] args) {
    String jsonString =
        JSON.toJSONString(
            new HashMap<String, String>() {
              {
                put("name", "zhangsan");
                put("id", "343");
              }
            });
    String encoded = JwtHelper.encode(jsonString, new RsaSigner(PRIVATE_KEY)).getEncoded();
//    System.out.println(encoded);
    //        Jwt decode =
    // JwtHelper.decode("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.aGhoaGhoaGhoaGhoaGhoaA.NXL4cJ9zMkKmaT2JnuYmr_sMRm51mil5ueje73NP5s96pOWPdHgUU875iFL-DabNu3hYOGEjO47rWnxTjzug9S_XOry7aAcKFA-cN3ROAD8rXON-dIH0gNnBYYcIWzcTAfvtGCNQjUrXyL4nxypBqog5Plw8k7V-6hS1L4PZYnM");
    Jwt decode = JwtHelper.decodeAndVerify(encoded, new RsaVerifier(PUBLIC_KEY));
    System.out.println(decode.getClaims());
  }
}
