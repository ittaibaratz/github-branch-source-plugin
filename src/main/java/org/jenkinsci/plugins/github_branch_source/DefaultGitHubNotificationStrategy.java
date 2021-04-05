/*
 * The MIT License
 *
 * Copyright 2017 Steven Foster
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkinsci.plugins.github_branch_source;

import hudson.model.Cause;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Default implementation of {@link AbstractGitHubNotificationStrategy}
 *
 * @since 2.3.2
 */
public final class DefaultGitHubNotificationStrategy extends AbstractGitHubNotificationStrategy {

  private static final Logger LOGGER =
      Logger.getLogger(DefaultGitHubNotificationStrategy.class.getName());

  private void logHosts(GitHubNotificationContext notificationContext) {
    String primaryHost = System.getenv("PRIMARY_HOSTNAME");
    String host = System.getenv("HOSTNAME");
    if (primaryHost != null) {
      LOGGER.info("[LOG HOSTS] Primary host is " + primaryHost);
    } else {
      LOGGER.info("[LOG HOSTS] Primary host not set");
    }

    if (host != null) {
      LOGGER.info("[LOG HOSTS] This host is " + host);
    } else {
      LOGGER.info("[LOG HOSTS] This host is not set");
    }
    Job<?, ?> job = notificationContext.getJob();
    if (job != null) {
      LOGGER.info("[LOG HOSTS] The job is " + job.getName());
    } else {
      LOGGER.info("[LOG HOSTS] job is not set");
    }
    Run<?, ?> build = notificationContext.getBuild();
    if (build != null) {
      List<Cause> causes = build.getCauses();
      if (causes != null) {
        for (Cause cause : causes) {
          LOGGER.info("[LOG HOSTS] Build cause: " + cause.getShortDescription());
        }
      }
    } else {
      LOGGER.info("[LOG HOSTS] No build");
    }
  }

  /** {@inheritDoc} */
  public List<GitHubNotificationRequest> notifications(
      GitHubNotificationContext notificationContext, TaskListener listener) {
    // For now we are just going to log the primary/current ENVs and build cause.
    this.logHosts(notificationContext);

    return Collections.singletonList(
        GitHubNotificationRequest.build(
            notificationContext.getDefaultContext(listener),
            notificationContext.getDefaultUrl(listener),
            notificationContext.getDefaultMessage(listener),
            notificationContext.getDefaultState(listener),
            notificationContext.getDefaultIgnoreError(listener)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    return this == o || (o != null && getClass() == o.getClass());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return 42;
  }
}
