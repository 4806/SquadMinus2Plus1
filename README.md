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
This is a class project for the course SYSC 4806 at Carleton University. Teams are given three 2-week sprints to complete the project. The focus of the project is to apply engineering methods and design principles taught in the course towards the development of a Web Application, using the Spring MVC framework. The project must be conducted using Agile methodologies as well as have Continuous Integration and Deployment. 

_Social Wiki:_ <br />. 
It’s a mix of a wiki and a simple social network: a user can publish a wiki page, “like” an existing page, and “follow” another user. Editing an existing page doesn’t modify the current page; instead it creates a new page with the new content, with a “parent” link to the original page that was edited. This results in a tree of versions of pages, not unlike git. One should be able to query for all wiki pages with the same entry, and sort them using various criteria, e.g.: most “liked”, most recent, most edited, liked by the most popular user (i.e. the user who has been “followed” the most), liked by the closest user in the “follow” graph, liked by the most similar user in terms of documents they both liked. One can also lookup a user profile and see which pages they’ve created or liked, and which other users they follow.

## Backlog
_Current Sprint: **3**_ <br />

_Current Progress_ <br />
The focus of the third and final sprint is to fix all bugs currently in the system as well as make various small improvements to improve the user experience. Major use case items that will be addressed are tracking the pages a user creates, the ability to follow users, being notified of followed users activity, delete a user account, and using statistics to make a more personal user experience.  

### Product Backlog
_Final Milestone, all backlog items will attempt to be completed_

### Sprint Backlog
- As a wiki user, I want my user profile to display all the contributions that I’ve made on the wiki, so that other users can find my work.
- As a wiki user, I want to be able to delete my account, so that I can stop using the service if I want to.
- As a wiki user, I want to follow other users, so that I can get notified on the actions of other users via an email
- As a wiki user, I want to be notified of the actions of my followed users, so that I can see when they make new actions.
- As a wiki user, I want to customize how I receive notifications, so that I am not notified of actions I am uninterested in.
- As a wiki user, I want my user profile to display which other users I follow, so that other users can see who I am interested in.
- As a wiki user, I want to be able to see statistics on my activity and the activity of other users, so I can see which pages I and other users have visited and contributed to most.
- As a wiki user, I want to be able to see statistics of wiki pages, so that I can see how many times its been visited and how many contributions it’s had.


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
- As a wiki user, I want to filter my searches/queries, so that I can find specific pages faster.
- As a guest, I want to filter my searches/queries, so that I can find specific pages faster.
- As a wiki user, I want to be able to like pages, so that I can go back to them at a later date.
- As a wiki user, I want to be able to have a customizable profile, so that I can differentiate myself from other users.
- As a wiki user, I want my user profile to display which pages I’ve liked, so that other users can see what kinds of pages I am interested in.
- As a wiki user, I want to be able to see the complete history of a wiki page, so I can see who made what changes when.
- As a wiki user, I want to see my profile page when I first enter the site, so that I can more easily reach all my favourite pages.
- Add ability to go to parent page from any child page.
- Add ability to persist past program close.