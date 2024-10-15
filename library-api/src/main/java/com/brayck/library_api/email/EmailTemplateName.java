package com.brayck.library_api.email;

public enum EmailTemplateName {
   ACTIVATE_ACCOUNT("activate_account"),
   FORGOT_PASSWORD("forgot_password");

   private final String value;

   EmailTemplateName(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }
}
