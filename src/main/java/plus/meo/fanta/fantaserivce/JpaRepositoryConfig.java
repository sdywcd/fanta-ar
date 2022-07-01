package plus.meo.fanta.fantaserivce;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by sdywcd on 2019-06-17.
 * Des:
 */
@Configuration
@EnableJpaRepositories(basePackages = {"plus.meo.fanta.fantaserivce.repo"})
public class JpaRepositoryConfig {
}
