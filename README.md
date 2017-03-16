# SquadMinus2Plus1
_SYSC 4806 Project Repository_

Heroku link: https://socialwiki-4806.herokuapp.com/

Travis Build Status: 
[![Build Status](https://travis-ci.com/4806/SquadMinus2Plus1.svg?token=qmpV8nydhg7CsjJzighq&branch=master)](https://travis-ci.com/4806/SquadMinus2Plus1)

## Team
Chris Briglio 100890068 <br />
Connor Matthews 100892794 <br />
David Briglio 100890858

## Project Description
_Social Wiki:_ <br />
It’s a mix of a wiki and a simple social network: a user can publish a wiki page, “like” an existing page, and “follow” another user. Editing an existing page doesn’t modify the current page; instead it creates a new page with the new content, with a “parent” link to the original page that was edited. This results in a tree of versions of pages, not unlike git. One should be able to query for all wiki pages with the same entry, and sort them using various criteria, e.g.: most “liked”, most recent, most edited, liked by the most popular user (i.e. the user who has been “followed” the most), liked by the closest user in the “follow” graph, liked by the most similar user in terms of documents they both liked. One can also lookup a user profile and see which pages they’ve created or liked, and which other users they follow.

## Backlog
_Current Sprint: **2**_ <br />

_Current Progress_ <br />
The focus of the second sprint is to add additional features to the program, as well as improvements on security, testing, and persistence. This will include the abilities to better filter searches, have user profiles, view wiki page history, delete accounts, like pages, retain persistence beyond program close, testing of the UI, and better authentication methods for user.  

### Product Backlog
- As a wiki user, I want to follow other users, so that I can get notified on the actions of other users via an email
- As a wiki user, I want to be notified of the actions of my followed users, so that I can see when they make new actions.
- As a wiki user, I want to customize how I receive notifications, so that I am not notified of actions I am uninterested in.
- As a wiki user, I want my user profile to display which other users I follow, so that other users can see who I am interested in.
- As a wiki user, I want to be able to see statistics on my activity and the activity of other users, so I can see which pages I and other users have visited and contributed to most.
- As a wiki user, I want to be able to see statistics of wiki pages, so that I can see how many times its been visited and how many contributions it’s had.

### Sprint Backlog
- As a wiki user, I want to filter my searches/queries, so that I can find specific pages faster.
- As a guest, I want to filter my searches/queries, so that I can find specific pages faster.
- As a wiki user, I want to be able to like pages, so that I can go back to them at a later date.
- As a wiki user, I want to be able to have a customizable profile, so that I can differentiate myself from other users.
- As a wiki user, I want my user profile to display all the contributions that I’ve made on the wiki, so that other users can find my work.
- As a wiki user, I want my user profile to display which pages I’ve liked, so that other users can see what kinds of pages I am interested in.
- As a wiki user, I want to be able to see the complete history of a wiki page, so I can see who made what changes when.
- As a wiki user, I want to be able to delete my account, so that I can stop using the service if I want to.
- As a wiki user, I want to see my profile page when I first enter the site, so that I can more easily reach all my favourite pages.
- Add ability to go to parent page from any child page.
- Add ability to persist past program close.

### Completed
- As a wiki user I want to login, so that I can make contributions and changes to my profile.
- As a wiki user, I want the site to remember who I am from page to page, so that I don’t have to login every time I want to make an action.
- As a wiki user, I want to be able to make a new page, so that I can add content.
- As a wiki user, I want to be able to edit an existing page, so that I can make contributions to existing work.
- As a wiki user, I want to search/query for pages, so that I can find specific pages I’m looking for.
- As a guest, I want to see the description of the website when I first visit it, so that I can get a good idea of what the wiki is about and how it works.
- As a guest, I want to be able to access a signup page, so that I can make an account if I want to.
- As a guest, I want to be able to view pages, so that I can view content without an account.
- As a guest, I want to be able to search/query for pages, so that I can look for specific pages without an account.