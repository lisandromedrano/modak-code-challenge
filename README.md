# Notification Service

## Overview

This project implements a rate-limited notification service using Kotlin. The service sends out email notifications of various types (status updates, daily news, project invitations, etc.). To protect recipients from receiving too many emails due to system errors or abuse, the service limits the number of emails sent to each recipient based on specific rate limit rules.

## Features

- **Rate Limiting:** Implements rate limiting rules to control the frequency of email notifications sent to each recipient.
- **Notification Types:** Supports various types of notifications with different rate limit rules.
- **Flexible Configuration:** Rate limit rules and notification types can be extended and customized.

## Rate Limiting Rules

The rate limit rules are defined for each notification type. Some example rules include:

- **Status:** Not more than 2 per minute for each recipient.
- **News:** Not more than 1 per day for each recipient.
- **Marketing:** Not more than 3 per hour for each recipient.

These are just examples, and the system might have several other rate limit rules.
