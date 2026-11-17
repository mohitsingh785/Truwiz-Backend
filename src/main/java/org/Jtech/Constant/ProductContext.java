package org.Jtech.Constant;

/** What kind of product is being scored. Built by ScoreService from the category. */
public record ProductContext(ConcernArea area, ContactType contactType) {

    public ProductContext {
        area        = area == null ? ConcernArea.SKIN : area;
        contactType = contactType == null ? ContactType.LEAVE_ON : contactType;
    }
}
