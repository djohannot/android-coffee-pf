package com.ysdc.coffee.injection.annotations;

import javax.inject.Qualifier;

public class Qualifiers {

    @Qualifier
    public @interface ForApplication {
    }

    @Qualifier
    public @interface ForActivity {
    }

}
