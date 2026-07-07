package dev.clutcher.modulith.archunit.examples.hexagonal.invalid.wrongLoggerFieldName.in.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterWithWrongLoggerName {

    private static final Logger log = LoggerFactory.getLogger(AdapterWithWrongLoggerName.class);
}
