# SquadMinus2Plus1 #
_SYSC 4806 Project Repository_

## Team
Chris Briglio 100890068 <br />
Connor Matthews 100892794 <br />
David Briglio 100890858

## Project Description ##
_Social Wiki:_ <br />
It’s a mix of a wiki and a simple social network: a user can publish a wiki page, “like” an existing page, and “follow” another user. Editing an existing page doesn’t modify the current page; instead it creates a new page with the new content, with a “parent” link to the original page that was edited. This results in a tree of versions of pages, not unlike git. One should be able to query for all wiki pages with the same entry, and sort them using various criteria, e.g.: most “liked”, most recent, most edited, liked by the most popular user (i.e. the user who has been “followed” the most), liked by the closest user in the “follow” graph, liked by the most similar user in terms of documents they both liked. One can also lookup a user profile and see which pages they’ve created or liked, and which other users they follow.
