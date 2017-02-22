package filters;

import javax.inject.Inject;

import org.pac4j.play.filters.SecurityFilter;

import deadline.DeadlineFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;

public class Filters implements HttpFilters {

    private final SecurityFilter securityFilter;
    private final DeadlineFilter deadlineFilter;
    private final CSRFFilter     csrfFilter;

    @Inject
    public Filters(SecurityFilter securityFilter, DeadlineFilter deadlineFilter,
            CSRFFilter csrfFilter) {
        this.securityFilter = securityFilter;
        this.deadlineFilter = deadlineFilter;
        this.csrfFilter = csrfFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] { deadlineFilter, securityFilter.asJava(),
                csrfFilter.asJava() };
    }
}
