



<!DOCTYPE html>
<html lang="en" class="">
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# object: http://ogp.me/ns/object# article: http://ogp.me/ns/article# profile: http://ogp.me/ns/profile#">
    <meta charset='utf-8'>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    
    
    <title>FrameViewer-AnTracks/TimeFormatter.java at f1034d291ca82e669f81cd4ecbde602dd4d8cd69 · samcrow/FrameViewer-AnTracks</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-144.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144.png">
    <meta property="fb:app_id" content="1401488693436528">

      <meta content="@github" name="twitter:site" /><meta content="summary" name="twitter:card" /><meta content="samcrow/FrameViewer-AnTracks" name="twitter:title" /><meta content="FrameViewer-AnTracks - A fork of FrameViewer that integrates with data from AnTracks" name="twitter:description" /><meta content="https://avatars0.githubusercontent.com/u/1839330?v=2&amp;s=400" name="twitter:image:src" />
<meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="https://avatars0.githubusercontent.com/u/1839330?v=2&amp;s=400" property="og:image" /><meta content="samcrow/FrameViewer-AnTracks" property="og:title" /><meta content="https://github.com/samcrow/FrameViewer-AnTracks" property="og:url" /><meta content="FrameViewer-AnTracks - A fork of FrameViewer that integrates with data from AnTracks" property="og:description" />

      <meta name="browser-stats-url" content="/_stats">
    <link rel="assets" href="https://assets-cdn.github.com/">
    <link rel="conduit-xhr" href="https://ghconduit.com:25035">
    <link rel="xhr-socket" href="/_sockets">
    <meta name="pjax-timeout" content="1000">
    <link rel="sudo-modal" href="/sessions/sudo_modal">

    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="selected-link" value="repo_source" data-pjax-transient>
      <meta name="google-analytics" content="UA-3769691-2">

    <meta content="collector.githubapp.com" name="octolytics-host" /><meta content="collector-cdn.github.com" name="octolytics-script-host" /><meta content="github" name="octolytics-app-id" /><meta content="80788DC5:6BA8:43FD1B5:545C123A" name="octolytics-dimension-request_id" /><meta content="3068163" name="octolytics-actor-id" /><meta content="jd4cubs" name="octolytics-actor-login" /><meta content="2065e60732dc81ba3755dc27e5642f30a325c74eb1a62362cd9e3bf1df2b1d10" name="octolytics-actor-hash" />
    
    <meta content="Rails, view, blob#show" name="analytics-event" />

    
    
    <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">


    <meta content="authenticity_token" name="csrf-param" />
<meta content="66ha9rcC6Tqru3KeUSa6Co6Fw73iW7WoC04F5P4PiC0A1xKBTnvyguM7YaBFgmeyXEQWh5JHwrVrmfcaBm/5Vg==" name="csrf-token" />

    <link href="https://assets-cdn.github.com/assets/github-c4d873b9ded699b9b32aef10aa435c2e037b56e0b5b0c32e818509fd83beb06e.css" media="all" rel="stylesheet" type="text/css" />
    <link href="https://assets-cdn.github.com/assets/github2-0e9f4887cb1a69b1b723816e1a67bc0b769e8476406c0a0140d3a0c9b86b7fc8.css" media="all" rel="stylesheet" type="text/css" />
    
    


    <meta http-equiv="x-pjax-version" content="3049db9cf44f7197e64d7fc1258b0d57">

      
  <meta name="description" content="FrameViewer-AnTracks - A fork of FrameViewer that integrates with data from AnTracks">
  <meta name="go-import" content="github.com/samcrow/FrameViewer-AnTracks git https://github.com/samcrow/FrameViewer-AnTracks.git">

  <meta content="1839330" name="octolytics-dimension-user_id" /><meta content="samcrow" name="octolytics-dimension-user_login" /><meta content="23317684" name="octolytics-dimension-repository_id" /><meta content="samcrow/FrameViewer-AnTracks" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="23317684" name="octolytics-dimension-repository_network_root_id" /><meta content="samcrow/FrameViewer-AnTracks" name="octolytics-dimension-repository_network_root_nwo" />
  <link href="https://github.com/samcrow/FrameViewer-AnTracks/commits/f1034d291ca82e669f81cd4ecbde602dd4d8cd69.atom" rel="alternate" title="Recent Commits to FrameViewer-AnTracks:f1034d291ca82e669f81cd4ecbde602dd4d8cd69" type="application/atom+xml">

  </head>


  <body class="logged_in  env-production windows vis-public page-blob">
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
    <div class="wrapper">
      
      
      
      


      <div class="header header-logged-in true" role="banner">
  <div class="container clearfix">

    <a class="header-logo-invertocat" href="https://github.com/" data-hotkey="g d" aria-label="Homepage" ga-data-click="Header, go to dashboard, icon:logo">
  <span class="mega-octicon octicon-mark-github"></span>
</a>


      <div class="site-search repo-scope js-site-search" role="search">
          <form accept-charset="UTF-8" action="/samcrow/FrameViewer-AnTracks/search" class="js-site-search-form" data-global-search-url="/search" data-repo-search-url="/samcrow/FrameViewer-AnTracks/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
  <input type="text"
    class="js-site-search-field is-clearable"
    data-hotkey="s"
    name="q"
    placeholder="Search"
    data-global-scope-placeholder="Search GitHub"
    data-repo-scope-placeholder="Search"
    tabindex="1"
    autocapitalize="off">
  <div class="scope-badge">This repository</div>
</form>
      </div>
      <ul class="header-nav left" role="navigation">
        <li class="header-nav-item explore">
          <a class="header-nav-link" href="/explore" data-ga-click="Header, go to explore, text:explore">Explore</a>
        </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://gist.github.com" data-ga-click="Header, go to gist, text:gist">Gist</a>
          </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="/blog" data-ga-click="Header, go to blog, text:blog">Blog</a>
          </li>
        <li class="header-nav-item">
          <a class="header-nav-link" href="https://help.github.com" data-ga-click="Header, go to help, text:help">Help</a>
        </li>
      </ul>

    
<ul class="header-nav user-nav right" id="user-links">
  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link name" href="/jd4cubs" data-ga-click="Header, go to profile, text:username">
      <img alt="jd4cubs" class="avatar" data-user="3068163" height="20" src="https://avatars0.githubusercontent.com/u/3068163?v=2&amp;s=40" width="20" />
      <span class="css-truncate">
        <span class="css-truncate-target">jd4cubs</span>
      </span>
    </a>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link js-menu-target tooltipped tooltipped-s" href="#" aria-label="Create new..." data-ga-click="Header, create new, icon:add">
      <span class="octicon octicon-plus"></span>
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      
<ul class="dropdown-menu">
  <li>
    <a href="/new"><span class="octicon octicon-repo"></span> New repository</a>
  </li>
  <li>
    <a href="/organizations/new"><span class="octicon octicon-organization"></span> New organization</a>
  </li>


    <li class="dropdown-divider"></li>
    <li class="dropdown-header">
      <span title="samcrow/FrameViewer-AnTracks">This repository</span>
    </li>
      <li>
        <a href="/samcrow/FrameViewer-AnTracks/issues/new"><span class="octicon octicon-issue-opened"></span> New issue</a>
      </li>
</ul>

    </div>
  </li>

  <li class="header-nav-item">
        <a href="/notifications" aria-label="You have no unread notifications" class="header-nav-link notification-indicator tooltipped tooltipped-s" data-ga-click="Header, go to notifications, icon:read" data-hotkey="g n">
        <span class="mail-status all-read"></span>
        <span class="octicon octicon-inbox"></span>
</a>
  </li>

  <li class="header-nav-item">
    <a class="header-nav-link tooltipped tooltipped-s" href="/settings/profile" id="account_settings" aria-label="Settings" data-ga-click="Header, go to settings, icon:settings">
      <span class="octicon octicon-gear"></span>
    </a>
  </li>

  <li class="header-nav-item">
    <form accept-charset="UTF-8" action="/logout" class="logout-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="cmSKcH5NWC+I8J5JeQ/R+B+GSIT2vsfd7k/DlYsJArtmam85ayWVRTL0gVbI7x1L0qWGD/HIQsQLuyP5/6rFng==" /></div>
      <button class="header-nav-link sign-out-button tooltipped tooltipped-s" aria-label="Sign out" data-ga-click="Header, sign out, icon:logout">
        <span class="octicon octicon-sign-out"></span>
      </button>
</form>  </li>

</ul>


    
  </div>
</div>

      

        


      <div id="start-of-content" class="accessibility-aid"></div>
          <div class="site" itemscope itemtype="http://schema.org/WebPage">
    <div id="js-flash-container">
      
    </div>
    <div class="pagehead repohead instapaper_ignore readability-menu">
      <div class="container">
        
<ul class="pagehead-actions">

    <li class="subscription">
      <form accept-charset="UTF-8" action="/notifications/subscribe" class="js-social-container" data-autosubmit="true" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="lWXZfsuuZa1VrP8e1I3WUxLnmH5stmQ9GMsKQ+erPC1VomgX/HHP8Q3Cl6X/cYqq6gpQLaxJklT+OAPT2eIoOw==" /></div>  <input id="repository_id" name="repository_id" type="hidden" value="23317684" />

    <div class="select-menu js-menu-container js-select-menu">
      <a class="social-count js-social-count" href="/samcrow/FrameViewer-AnTracks/watchers">
        1
      </a>
      <a href="/samcrow/FrameViewer-AnTracks/subscription"
        class="minibutton select-menu-button with-count js-menu-target" role="button" tabindex="0" aria-haspopup="true">
        <span class="js-select-button">
          <span class="octicon octicon-eye"></span>
          Watch
        </span>
      </a>

      <div class="select-menu-modal-holder">
        <div class="select-menu-modal subscription-menu-modal js-menu-content" aria-hidden="true">
          <div class="select-menu-header">
            <span class="select-menu-title">Notifications</span>
            <span class="octicon octicon-x js-menu-close" role="button" aria-label="Close"></span>
          </div> <!-- /.select-menu-header -->

          <div class="select-menu-list js-navigation-container" role="menu">

            <div class="select-menu-item js-navigation-item selected" role="menuitem" tabindex="0">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input checked="checked" id="do_included" name="do" type="radio" value="included" />
                <h4>Not watching</h4>
                <span class="description">Be notified when participating or @mentioned.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-eye"></span>
                  Watch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
              <span class="select-menu-item-icon octicon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input id="do_subscribed" name="do" type="radio" value="subscribed" />
                <h4>Watching</h4>
                <span class="description">Be notified of all conversations.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-eye"></span>
                  Unwatch
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

            <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <div class="select-menu-item-text">
                <input id="do_ignore" name="do" type="radio" value="ignore" />
                <h4>Ignoring</h4>
                <span class="description">Never be notified.</span>
                <span class="js-select-button-text hidden-select-button-text">
                  <span class="octicon octicon-mute"></span>
                  Stop ignoring
                </span>
              </div>
            </div> <!-- /.select-menu-item -->

          </div> <!-- /.select-menu-list -->

        </div> <!-- /.select-menu-modal -->
      </div> <!-- /.select-menu-modal-holder -->
    </div> <!-- /.select-menu -->

</form>
    </li>

  <li>
    
  <div class="js-toggler-container js-social-container starring-container ">

    <form accept-charset="UTF-8" action="/samcrow/FrameViewer-AnTracks/unstar" class="js-toggler-form starred js-unstar-button" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="w2UDJWR7deOjfzdBGfJnePj9xVxMldDa1WCpECVVj3eMjkUe1D1fEoYXlE9xoFQLexr2fzh58UG10SSLSZa3cg==" /></div>
      <button
        class="minibutton with-count js-toggler-target star-button"
        aria-label="Unstar this repository" title="Unstar samcrow/FrameViewer-AnTracks">
        <span class="octicon octicon-star"></span>
        Unstar
      </button>
        <a class="social-count js-social-count" href="/samcrow/FrameViewer-AnTracks/stargazers">
          0
        </a>
</form>
    <form accept-charset="UTF-8" action="/samcrow/FrameViewer-AnTracks/star" class="js-toggler-form unstarred js-star-button" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="TMa7b5Gby3UWa8RPz3MDy2cl6pVOxgaXVGZSqpMPpWAyek3YlHTEdErEsQu3W+qXzXxI4IdDP5bu3SwEQu1Paw==" /></div>
      <button
        class="minibutton with-count js-toggler-target star-button"
        aria-label="Star this repository" title="Star samcrow/FrameViewer-AnTracks">
        <span class="octicon octicon-star"></span>
        Star
      </button>
        <a class="social-count js-social-count" href="/samcrow/FrameViewer-AnTracks/stargazers">
          0
        </a>
</form>  </div>

  </li>


        <li>
          <a href="/samcrow/FrameViewer-AnTracks/fork" class="minibutton with-count js-toggler-target fork-button tooltipped-n" title="Fork your own copy of samcrow/FrameViewer-AnTracks to your account" aria-label="Fork your own copy of samcrow/FrameViewer-AnTracks to your account" rel="nofollow" data-method="post">
            <span class="octicon octicon-repo-forked"></span>
            Fork
          </a>
          <a href="/samcrow/FrameViewer-AnTracks/network" class="social-count">1</a>
        </li>

</ul>

        <h1 itemscope itemtype="http://data-vocabulary.org/Breadcrumb" class="entry-title public">
          <span class="mega-octicon octicon-repo"></span>
          <span class="author"><a href="/samcrow" class="url fn" itemprop="url" rel="author"><span itemprop="title">samcrow</span></a></span><!--
       --><span class="path-divider">/</span><!--
       --><strong><a href="/samcrow/FrameViewer-AnTracks" class="js-current-repository js-repo-home-link" data-pjax-container-selector="#js-repo-pjax-container">FrameViewer-AnTracks</a></strong>

          <span class="page-context-loader">
            <img alt="" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
          </span>

        </h1>
      </div><!-- /.container -->
    </div><!-- /.repohead -->

    <div class="container">
      <div class="repository-with-sidebar repo-container new-discussion-timeline  ">
        <div class="repository-sidebar clearfix">
            
<nav class="sunken-menu repo-nav js-repo-nav js-sidenav-container-pjax js-octicon-loaders" role="navigation" data-issue-count-url="/samcrow/FrameViewer-AnTracks/issues/counts" data-pjax-container-selector="#js-repo-pjax-container">
  <ul class="sunken-menu-group">
    <li class="tooltipped tooltipped-w" aria-label="Code">
      <a href="/samcrow/FrameViewer-AnTracks" aria-label="Code" class="selected js-selected-navigation-item sunken-menu-item" data-hotkey="g c" data-pjax="true" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /samcrow/FrameViewer-AnTracks">
        <span class="octicon octicon-code"></span> <span class="full-word">Code</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

      <li class="tooltipped tooltipped-w" aria-label="Issues">
        <a href="/samcrow/FrameViewer-AnTracks/issues" aria-label="Issues" class="js-selected-navigation-item sunken-menu-item js-disable-pjax" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /samcrow/FrameViewer-AnTracks/issues">
          <span class="octicon octicon-issue-opened"></span> <span class="full-word">Issues</span>
          <span class="js-issue-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>

    <li class="tooltipped tooltipped-w" aria-label="Pull Requests">
      <a href="/samcrow/FrameViewer-AnTracks/pulls" aria-label="Pull Requests" class="js-selected-navigation-item sunken-menu-item js-disable-pjax" data-hotkey="g p" data-selected-links="repo_pulls /samcrow/FrameViewer-AnTracks/pulls">
          <span class="octicon octicon-git-pull-request"></span> <span class="full-word">Pull Requests</span>
          <span class="js-pull-replace-counter"></span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>


      <li class="tooltipped tooltipped-w" aria-label="Wiki">
        <a href="/samcrow/FrameViewer-AnTracks/wiki" aria-label="Wiki" class="js-selected-navigation-item sunken-menu-item js-disable-pjax" data-hotkey="g w" data-selected-links="repo_wiki /samcrow/FrameViewer-AnTracks/wiki">
          <span class="octicon octicon-book"></span> <span class="full-word">Wiki</span>
          <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>      </li>
  </ul>
  <div class="sunken-menu-separator"></div>
  <ul class="sunken-menu-group">

    <li class="tooltipped tooltipped-w" aria-label="Pulse">
      <a href="/samcrow/FrameViewer-AnTracks/pulse/weekly" aria-label="Pulse" class="js-selected-navigation-item sunken-menu-item" data-pjax="true" data-selected-links="pulse /samcrow/FrameViewer-AnTracks/pulse/weekly">
        <span class="octicon octicon-pulse"></span> <span class="full-word">Pulse</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>

    <li class="tooltipped tooltipped-w" aria-label="Graphs">
      <a href="/samcrow/FrameViewer-AnTracks/graphs" aria-label="Graphs" class="js-selected-navigation-item sunken-menu-item" data-pjax="true" data-selected-links="repo_graphs repo_contributors /samcrow/FrameViewer-AnTracks/graphs">
        <span class="octicon octicon-graph"></span> <span class="full-word">Graphs</span>
        <img alt="" class="mini-loader" height="16" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-32.gif" width="16" />
</a>    </li>
  </ul>


</nav>

              <div class="only-with-full-nav">
                
  
<div class="clone-url open"
  data-protocol-type="http"
  data-url="/users/set_protocol?protocol_selector=http&amp;protocol_type=clone">
  <h3><span class="text-emphasized">HTTPS</span> clone URL</h3>
  <div class="input-group">
    <input type="text" class="input-mini input-monospace js-url-field"
           value="https://github.com/samcrow/FrameViewer-AnTracks.git" readonly="readonly">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-clipboard-text="https://github.com/samcrow/FrameViewer-AnTracks.git" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>

  
<div class="clone-url "
  data-protocol-type="ssh"
  data-url="/users/set_protocol?protocol_selector=ssh&amp;protocol_type=clone">
  <h3><span class="text-emphasized">SSH</span> clone URL</h3>
  <div class="input-group">
    <input type="text" class="input-mini input-monospace js-url-field"
           value="git@github.com:samcrow/FrameViewer-AnTracks.git" readonly="readonly">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-clipboard-text="git@github.com:samcrow/FrameViewer-AnTracks.git" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>

  
<div class="clone-url "
  data-protocol-type="subversion"
  data-url="/users/set_protocol?protocol_selector=subversion&amp;protocol_type=clone">
  <h3><span class="text-emphasized">Subversion</span> checkout URL</h3>
  <div class="input-group">
    <input type="text" class="input-mini input-monospace js-url-field"
           value="https://github.com/samcrow/FrameViewer-AnTracks" readonly="readonly">
    <span class="input-group-button">
      <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-clipboard-text="https://github.com/samcrow/FrameViewer-AnTracks" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
    </span>
  </div>
</div>


<p class="clone-options">You can clone with
      <a href="#" class="js-clone-selector" data-protocol="http">HTTPS</a>,
      <a href="#" class="js-clone-selector" data-protocol="ssh">SSH</a>,
      or <a href="#" class="js-clone-selector" data-protocol="subversion">Subversion</a>.
  <a href="https://help.github.com/articles/which-remote-url-should-i-use" class="help tooltipped tooltipped-n" aria-label="Get help on which URL is right for you.">
    <span class="octicon octicon-question"></span>
  </a>
</p>


  <a href="github-windows://openRepo/https://github.com/samcrow/FrameViewer-AnTracks" class="minibutton sidebar-button" title="Save samcrow/FrameViewer-AnTracks to your computer and use it in GitHub Desktop." aria-label="Save samcrow/FrameViewer-AnTracks to your computer and use it in GitHub Desktop.">
    <span class="octicon octicon-device-desktop"></span>
    Clone in Desktop
  </a>

                <a href="/samcrow/FrameViewer-AnTracks/archive/f1034d291ca82e669f81cd4ecbde602dd4d8cd69.zip"
                   class="minibutton sidebar-button"
                   aria-label="Download the contents of samcrow/FrameViewer-AnTracks as a zip file"
                   title="Download the contents of samcrow/FrameViewer-AnTracks as a zip file"
                   rel="nofollow">
                  <span class="octicon octicon-cloud-download"></span>
                  Download ZIP
                </a>
              </div>
        </div><!-- /.repository-sidebar -->

        <div id="js-repo-pjax-container" class="repository-content context-loader-container" data-pjax-container>
          

<a href="/samcrow/FrameViewer-AnTracks/blob/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java" class="hidden js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:f9d5eb97b48c13ac12da3fd1458fc69b -->

<div class="file-navigation">
  
<div class="select-menu js-menu-container js-select-menu left">
  <span class="minibutton select-menu-button js-menu-target css-truncate" data-hotkey="w"
    data-master-branch="master"
    data-ref=""
    title=""
    role="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
    <span class="octicon octicon-git-branch"></span>
    <i>tree:</i>
    <span class="js-select-button css-truncate-target">f1034d291c</span>
  </span>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax aria-hidden="true">

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <span class="select-menu-title">Switch branches/tags</span>
        <span class="octicon octicon-x js-menu-close" role="button" aria-label="Close"></span>
      </div> <!-- /.select-menu-header -->

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Filter branches/tags" id="context-commitish-filter-field" class="js-filterable-field js-navigation-enable" placeholder="Filter branches/tags">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" class="js-select-menu-tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" class="js-select-menu-tab">Tags</a>
            </li>
          </ul>
        </div><!-- /.select-menu-tabs -->
      </div><!-- /.select-menu-filters -->

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/blob/autosync/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="autosync"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="autosync">autosync</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/blob/edit/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="edit"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="edit">edit</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/blob/master/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="master"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="master">master</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/blob/new-move-type-legacy/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="new-move-type-legacy"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="new-move-type-legacy">new-move-type-legacy</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/blob/schema2/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="schema2"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="schema2">schema2</a>
            </div> <!-- /.select-menu-item -->
        </div>

          <div class="select-menu-no-results">Nothing to show</div>
      </div> <!-- /.select-menu-list -->

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.1.0beta1-fix/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.1.0beta1-fix"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.1.0beta1-fix">v2.1.0beta1-fix</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.1.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.1.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.1.0beta1">v2.1.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.0.2beta1-nmt/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.0.2beta1-nmt"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.0.2beta1-nmt">v2.0.2beta1-nmt</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.0.2beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.0.2beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.0.2beta1">v2.0.2beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.0.1beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.0.1beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.0.1beta1">v2.0.1beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v2.0.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v2.0.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v2.0.0beta1">v2.0.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/v1.0/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="v1.0"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="v1.0">v1.0</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.2beta1-bumped/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.2beta1-bumped"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.2beta1-bumped">1.5.2beta1-bumped</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.2beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.2beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.2beta1">1.5.2beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.1beta4/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.1beta4"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.1beta4">1.5.1beta4</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.1beta3/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.1beta3"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.1beta3">1.5.1beta3</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.1beta2/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.1beta2"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.1beta2">1.5.1beta2</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.1beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.1beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.1beta1">1.5.1beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.0beta4/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.0beta4"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.0beta4">1.5.0beta4</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.0beta3/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.0beta3"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.0beta3">1.5.0beta3</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.0beta2/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.0beta2"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.0beta2">1.5.0beta2</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.5.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.5.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.5.0beta1">1.5.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.4.0beta1-bumped/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.4.0beta1-bumped"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.4.0beta1-bumped">1.4.0beta1-bumped</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.4.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.4.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.4.0beta1">1.4.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.3.1beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.3.1beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.3.1beta1">1.3.1beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.3.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.3.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.3.0beta1">1.3.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.2.1beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.2.1beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.2.1beta1">1.2.1beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.2.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.2.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.2.0beta1">1.2.0beta1</a>
            </div> <!-- /.select-menu-item -->
            <div class="select-menu-item js-navigation-item ">
              <span class="select-menu-item-icon octicon octicon-check"></span>
              <a href="/samcrow/FrameViewer-AnTracks/tree/1.1.0beta1/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java"
                 data-name="1.1.0beta1"
                 data-skip-pjax="true"
                 rel="nofollow"
                 class="js-navigation-open select-menu-item-text css-truncate-target"
                 title="1.1.0beta1">1.1.0beta1</a>
            </div> <!-- /.select-menu-item -->
        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div> <!-- /.select-menu-list -->

    </div> <!-- /.select-menu-modal -->
  </div> <!-- /.select-menu-modal-holder -->
</div> <!-- /.select-menu -->

  <div class="button-group right">
    <a href="/samcrow/FrameViewer-AnTracks/find/f1034d291ca82e669f81cd4ecbde602dd4d8cd69"
          class="js-show-file-finder minibutton empty-icon tooltipped tooltipped-s"
          data-pjax
          data-hotkey="t"
          aria-label="Quickly jump between files">
      <span class="octicon octicon-list-unordered"></span>
    </a>
    <button aria-label="Copy to clipboard" class="js-zeroclipboard minibutton zeroclipboard-button" data-clipboard-text="src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java" data-copied-hint="Copied!" type="button"><span class="octicon octicon-clippy"></span></button>
  </div>

  <div class="breadcrumb">
    <span class='repo-root js-repo-root'><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">FrameViewer-AnTracks</span></a></span></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">src</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">main</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">java</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">org</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">samcrow</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">frameviewer</span></a></span><span class="separator"> / </span><span itemscope="" itemtype="http://data-vocabulary.org/Breadcrumb"><a href="/samcrow/FrameViewer-AnTracks/tree/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer/util" class="" data-branch="f1034d291ca82e669f81cd4ecbde602dd4d8cd69" data-direction="back" data-pjax="true" itemscope="url" rel="nofollow"><span itemprop="title">util</span></a></span><span class="separator"> / </span><strong class="final-path">TimeFormatter.java</strong>
  </div>
</div>


  <div class="commit file-history-tease">
    <div class="file-history-tease-header">
        <img alt="" class="avatar" height="24" src="https://0.gravatar.com/avatar/84f9ad3e99e3dba9aa0a8345dc9cd956?d=https%3A%2F%2Fassets-cdn.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png&amp;r=x&amp;s=140" width="24" />
        <span class="author"><span>Sam Crow</span></span>
        <time datetime="2014-11-06T02:39:24Z" is="relative-time">Nov 5, 2014</time>
        <div class="commit-title">
            <a href="/samcrow/FrameViewer-AnTracks/commit/f1034d291ca82e669f81cd4ecbde602dd4d8cd69" class="message" data-pjax="true" title="Restructured test and updated things to work with Java 8">Restructured test and updated things to work with Java 8</a>
        </div>
    </div>

    <div class="participation">
      <p class="quickstat">
        <a href="#blob_contributors_box" rel="facebox">
          <strong>0</strong>
           contributors
        </a>
      </p>
      
    </div>
    <div id="blob_contributors_box" style="display:none">
      <h2 class="facebox-header">Users who have contributed to this file</h2>
      <ul class="facebox-user-list">
      </ul>
    </div>
  </div>

<div class="file-box">
  <div class="file">
    <div class="meta clearfix">
      <div class="info file-name">
          <span>61 lines (46 sloc)</span>
          <span class="meta-divider"></span>
        <span>1.849 kb</span>
      </div>
      <div class="actions">
        <div class="button-group">
          <a href="/samcrow/FrameViewer-AnTracks/raw/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java" class="minibutton " id="raw-url">Raw</a>
            <a href="/samcrow/FrameViewer-AnTracks/blame/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java" class="minibutton js-update-url-with-hash">Blame</a>
          <a href="/samcrow/FrameViewer-AnTracks/commits/f1034d291ca82e669f81cd4ecbde602dd4d8cd69/src/main/java/org/samcrow/frameviewer/util/TimeFormatter.java" class="minibutton " rel="nofollow">History</a>
        </div><!-- /.button-group -->


            <a class="octicon-button disabled tooltipped tooltipped-w" href="#"
               aria-label="You must be on a branch to make or propose changes to this file"><span class="octicon octicon-pencil"></span></a>

          <a class="octicon-button danger disabled tooltipped tooltipped-w" href="#"
             aria-label="You must be on a branch to make or propose changes to this file">
          <span class="octicon octicon-trashcan"></span>
        </a>
      </div><!-- /.actions -->
    </div>
    

  <div class="blob-wrapper data type-java">
      <table class="highlight tab-size-8 js-file-line-container">
      <tr>
        <td id="L1" class="blob-num js-line-number" data-line-number="1"></td>
        <td id="LC1" class="blob-code js-file-line"><span class="k">package</span> <span class="k">org.samcrow.frameviewer.util</span>;</td>
      </tr>
      <tr>
        <td id="L2" class="blob-num js-line-number" data-line-number="2"></td>
        <td id="LC2" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L3" class="blob-num js-line-number" data-line-number="3"></td>
        <td id="LC3" class="blob-code js-file-line"><span class="k">import</span> <span class="k">java.text.ParseException</span>;</td>
      </tr>
      <tr>
        <td id="L4" class="blob-num js-line-number" data-line-number="4"></td>
        <td id="LC4" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L5" class="blob-num js-line-number" data-line-number="5"></td>
        <td id="LC5" class="blob-code js-file-line"><span class="cm">/**<span class="h"></span></span></td>
      </tr>
      <tr>
        <td id="L6" class="blob-num js-line-number" data-line-number="6"></td>
        <td id="LC6" class="blob-code js-file-line"><span class="cm"><span class="h"> * Provides utilities for formatting time</span></span></td>
      </tr>
      <tr>
        <td id="L7" class="blob-num js-line-number" data-line-number="7"></td>
        <td id="LC7" class="blob-code js-file-line"><span class="cm"><span class="h"> * <span class="k">@author</span> Sam Crow</span></span></td>
      </tr>
      <tr>
        <td id="L8" class="blob-num js-line-number" data-line-number="8"></td>
        <td id="LC8" class="blob-code js-file-line"><span class="cm"><span class="h"> </span>*/</span></td>
      </tr>
      <tr>
        <td id="L9" class="blob-num js-line-number" data-line-number="9"></td>
        <td id="LC9" class="blob-code js-file-line"><span class="k">public</span> <span class="k">class</span> <span class="nc">TimeFormatter</span> {</td>
      </tr>
      <tr>
        <td id="L10" class="blob-num js-line-number" data-line-number="10"></td>
        <td id="LC10" class="blob-code js-file-line">    </td>
      </tr>
      <tr>
        <td id="L11" class="blob-num js-line-number" data-line-number="11"></td>
        <td id="LC11" class="blob-code js-file-line">    <span class="k">private</span> <span class="k">static</span> <span class="k">final</span> <span class="kt">double</span> <span class="no">FRAMES_PER_SECOND</span> <span class="o">=</span> <span class="m">29.97</span>;</td>
      </tr>
      <tr>
        <td id="L12" class="blob-num js-line-number" data-line-number="12"></td>
        <td id="LC12" class="blob-code js-file-line">
</td>
      </tr>
      <tr>
        <td id="L13" class="blob-num js-line-number" data-line-number="13"></td>
        <td id="LC13" class="blob-code js-file-line">    <span class="k">private</span> <span class="k">static</span> <span class="k">final</span> <span class="kt">int</span> <span class="no">SECONDS_PER_MINUTE</span> <span class="o">=</span> <span class="m">60</span>;</td>
      </tr>
      <tr>
        <td id="L14" class="blob-num js-line-number" data-line-number="14"></td>
        <td id="LC14" class="blob-code js-file-line">    </td>
      </tr>
      <tr>
        <td id="L15" class="blob-num js-line-number" data-line-number="15"></td>
        <td id="LC15" class="blob-code js-file-line">    <span class="cm">/**<span class="h"></span></span></td>
      </tr>
      <tr>
        <td id="L16" class="blob-num js-line-number" data-line-number="16"></td>
        <td id="LC16" class="blob-code js-file-line"><span class="cm"><span class="h">     * Returns a formatted duration with hours, minutes, and seconds</span></span></td>
      </tr>
      <tr>
        <td id="L17" class="blob-num js-line-number" data-line-number="17"></td>
        <td id="LC17" class="blob-code js-file-line"><span class="cm"><span class="h">     * <span class="k">@param</span> seconds The number of seconds to format</span></span></td>
      </tr>
      <tr>
        <td id="L18" class="blob-num js-line-number" data-line-number="18"></td>
        <td id="LC18" class="blob-code js-file-line"><span class="cm"><span class="h">     * <span class="k">@return</span> </span></span></td>
      </tr>
      <tr>
        <td id="L19" class="blob-num js-line-number" data-line-number="19"></td>
        <td id="LC19" class="blob-code js-file-line"><span class="cm"><span class="h">     </span>*/</span></td>
      </tr>
      <tr>
        <td id="L20" class="blob-num js-line-number" data-line-number="20"></td>
        <td id="LC20" class="blob-code js-file-line">    <span class="k">public</span> <span class="k">static</span> <span class="kt">String</span> <span class="nf">formatDuration</span>(<span class="kt">int</span> <span class="nv">seconds</span>) {</td>
      </tr>
      <tr>
        <td id="L21" class="blob-num js-line-number" data-line-number="21"></td>
        <td id="LC21" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L22" class="blob-num js-line-number" data-line-number="22"></td>
        <td id="LC22" class="blob-code js-file-line">        <span class="kt">int</span> minutes <span class="o">=</span> <span class="m">0</span>;</td>
      </tr>
      <tr>
        <td id="L23" class="blob-num js-line-number" data-line-number="23"></td>
        <td id="LC23" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L24" class="blob-num js-line-number" data-line-number="24"></td>
        <td id="LC24" class="blob-code js-file-line">        <span class="k">while</span>(seconds <span class="o">&gt;=</span> <span class="no">SECONDS_PER_MINUTE</span>) {</td>
      </tr>
      <tr>
        <td id="L25" class="blob-num js-line-number" data-line-number="25"></td>
        <td id="LC25" class="blob-code js-file-line">            minutes<span class="o">++</span>;</td>
      </tr>
      <tr>
        <td id="L26" class="blob-num js-line-number" data-line-number="26"></td>
        <td id="LC26" class="blob-code js-file-line">            seconds <span class="o">-=</span> <span class="no">SECONDS_PER_MINUTE</span>;</td>
      </tr>
      <tr>
        <td id="L27" class="blob-num js-line-number" data-line-number="27"></td>
        <td id="LC27" class="blob-code js-file-line">        }</td>
      </tr>
      <tr>
        <td id="L28" class="blob-num js-line-number" data-line-number="28"></td>
        <td id="LC28" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L29" class="blob-num js-line-number" data-line-number="29"></td>
        <td id="LC29" class="blob-code js-file-line">        <span class="k">return</span> <span class="kt">String</span><span class="o">.</span>format(<span class="s2">&quot;%02d:%02d&quot;</span>, minutes, seconds);</td>
      </tr>
      <tr>
        <td id="L30" class="blob-num js-line-number" data-line-number="30"></td>
        <td id="LC30" class="blob-code js-file-line">    }</td>
      </tr>
      <tr>
        <td id="L31" class="blob-num js-line-number" data-line-number="31"></td>
        <td id="LC31" class="blob-code js-file-line">    </td>
      </tr>
      <tr>
        <td id="L32" class="blob-num js-line-number" data-line-number="32"></td>
        <td id="LC32" class="blob-code js-file-line">    <span class="k">public</span> <span class="k">static</span> <span class="kt">String</span> <span class="nf">formatDurationFromFrame</span>(<span class="kt">int</span> <span class="nv">frame</span>) {</td>
      </tr>
      <tr>
        <td id="L33" class="blob-num js-line-number" data-line-number="33"></td>
        <td id="LC33" class="blob-code js-file-line">        <span class="k">return</span> formatDuration((<span class="kt">int</span>) <span class="kt">Math</span><span class="o">.</span>floor(frame <span class="o">/</span> <span class="no">FRAMES_PER_SECOND</span>));</td>
      </tr>
      <tr>
        <td id="L34" class="blob-num js-line-number" data-line-number="34"></td>
        <td id="LC34" class="blob-code js-file-line">    }</td>
      </tr>
      <tr>
        <td id="L35" class="blob-num js-line-number" data-line-number="35"></td>
        <td id="LC35" class="blob-code js-file-line">    </td>
      </tr>
      <tr>
        <td id="L36" class="blob-num js-line-number" data-line-number="36"></td>
        <td id="LC36" class="blob-code js-file-line">    <span class="cm">/**<span class="h"></span></span></td>
      </tr>
      <tr>
        <td id="L37" class="blob-num js-line-number" data-line-number="37"></td>
        <td id="LC37" class="blob-code js-file-line"><span class="cm"><span class="h">     * Parses a time string that contains a minutes value and a seconds value,</span></span></td>
      </tr>
      <tr>
        <td id="L38" class="blob-num js-line-number" data-line-number="38"></td>
        <td id="LC38" class="blob-code js-file-line"><span class="cm"><span class="h">     * separated by a colon character</span></span></td>
      </tr>
      <tr>
        <td id="L39" class="blob-num js-line-number" data-line-number="39"></td>
        <td id="LC39" class="blob-code js-file-line"><span class="cm"><span class="h">     * <span class="k">@param</span> time The duration string to parse</span></span></td>
      </tr>
      <tr>
        <td id="L40" class="blob-num js-line-number" data-line-number="40"></td>
        <td id="LC40" class="blob-code js-file-line"><span class="cm"><span class="h">     * <span class="k">@return</span> The number of seconds represented by the value</span></span></td>
      </tr>
      <tr>
        <td id="L41" class="blob-num js-line-number" data-line-number="41"></td>
        <td id="LC41" class="blob-code js-file-line"><span class="cm"><span class="h">     * <span class="k">@throws</span> ParseException If the duration string was not in a valid format </span></span></td>
      </tr>
      <tr>
        <td id="L42" class="blob-num js-line-number" data-line-number="42"></td>
        <td id="LC42" class="blob-code js-file-line"><span class="cm"><span class="h">     </span>*/</span></td>
      </tr>
      <tr>
        <td id="L43" class="blob-num js-line-number" data-line-number="43"></td>
        <td id="LC43" class="blob-code js-file-line">    <span class="k">public</span> <span class="k">static</span> <span class="kt">int</span> <span class="nf">parseDuration</span>(<span class="kt">String</span> <span class="nv">time</span>) <span class="k">throws</span> <span class="kt">ParseException</span> {</td>
      </tr>
      <tr>
        <td id="L44" class="blob-num js-line-number" data-line-number="44"></td>
        <td id="LC44" class="blob-code js-file-line">        <span class="k">try</span> {</td>
      </tr>
      <tr>
        <td id="L45" class="blob-num js-line-number" data-line-number="45"></td>
        <td id="LC45" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L46" class="blob-num js-line-number" data-line-number="46"></td>
        <td id="LC46" class="blob-code js-file-line">        <span class="kt">int</span> firstColonIndex <span class="o">=</span> time<span class="o">.</span>indexOf(<span class="s1">':'</span>);</td>
      </tr>
      <tr>
        <td id="L47" class="blob-num js-line-number" data-line-number="47"></td>
        <td id="LC47" class="blob-code js-file-line">        <span class="kt">int</span> min <span class="o">=</span> <span class="kt">Integer</span><span class="o">.</span>valueOf(time<span class="o">.</span>substring(<span class="m">0</span>, firstColonIndex));</td>
      </tr>
      <tr>
        <td id="L48" class="blob-num js-line-number" data-line-number="48"></td>
        <td id="LC48" class="blob-code js-file-line">        <span class="kt">int</span> sec <span class="o">=</span> <span class="kt">Integer</span><span class="o">.</span>valueOf(time<span class="o">.</span>substring(firstColonIndex <span class="o">+</span> <span class="m">1</span>));</td>
      </tr>
      <tr>
        <td id="L49" class="blob-num js-line-number" data-line-number="49"></td>
        <td id="LC49" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L50" class="blob-num js-line-number" data-line-number="50"></td>
        <td id="LC50" class="blob-code js-file-line">        <span class="k">return</span> (min <span class="o">*</span> <span class="no">SECONDS_PER_MINUTE</span>) <span class="o">+</span> sec;</td>
      </tr>
      <tr>
        <td id="L51" class="blob-num js-line-number" data-line-number="51"></td>
        <td id="LC51" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L52" class="blob-num js-line-number" data-line-number="52"></td>
        <td id="LC52" class="blob-code js-file-line">        </td>
      </tr>
      <tr>
        <td id="L53" class="blob-num js-line-number" data-line-number="53"></td>
        <td id="LC53" class="blob-code js-file-line">        }</td>
      </tr>
      <tr>
        <td id="L54" class="blob-num js-line-number" data-line-number="54"></td>
        <td id="LC54" class="blob-code js-file-line">        <span class="k">catch</span> (<span class="kt">NumberFormatException</span> <span class="o">|</span> <span class="kt">StringIndexOutOfBoundsException</span> ex) {</td>
      </tr>
      <tr>
        <td id="L55" class="blob-num js-line-number" data-line-number="55"></td>
        <td id="LC55" class="blob-code js-file-line">            <span class="kt">ParseException</span> exception <span class="o">=</span> <span class="k">new</span> <span class="kt">ParseException</span>(<span class="s2">&quot;Invalid duration string&quot;</span>, <span class="m">0</span>);</td>
      </tr>
      <tr>
        <td id="L56" class="blob-num js-line-number" data-line-number="56"></td>
        <td id="LC56" class="blob-code js-file-line">            exception<span class="o">.</span>initCause(ex);</td>
      </tr>
      <tr>
        <td id="L57" class="blob-num js-line-number" data-line-number="57"></td>
        <td id="LC57" class="blob-code js-file-line">            <span class="k">throw</span> exception;</td>
      </tr>
      <tr>
        <td id="L58" class="blob-num js-line-number" data-line-number="58"></td>
        <td id="LC58" class="blob-code js-file-line">        }</td>
      </tr>
      <tr>
        <td id="L59" class="blob-num js-line-number" data-line-number="59"></td>
        <td id="LC59" class="blob-code js-file-line">    }</td>
      </tr>
      <tr>
        <td id="L60" class="blob-num js-line-number" data-line-number="60"></td>
        <td id="LC60" class="blob-code js-file-line">}</td>
      </tr>
</table>

  </div>

  </div>
</div>

<a href="#jump-to-line" rel="facebox[.linejump]" data-hotkey="l" style="display:none">Jump to Line</a>
<div id="jump-to-line" style="display:none">
  <form accept-charset="UTF-8" class="js-jump-to-line-form">
    <input class="linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" autofocus>
    <button type="submit" class="button">Go</button>
  </form>
</div>

        </div>

      </div><!-- /.repo-container -->
      <div class="modal-backdrop"></div>
    </div><!-- /.container -->
  </div><!-- /.site -->


    </div><!-- /.wrapper -->

      <div class="container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links right">
      <li><a href="https://status.github.com/">Status</a></li>
      <li><a href="https://developer.github.com">API</a></li>
      <li><a href="http://training.github.com">Training</a></li>
      <li><a href="http://shop.github.com">Shop</a></li>
      <li><a href="/blog">Blog</a></li>
      <li><a href="/about">About</a></li>

    </ul>

    <a href="/" aria-label="Homepage">
      <span class="mega-octicon octicon-mark-github" title="GitHub"></span>
    </a>

    <ul class="site-footer-links">
      <li>&copy; 2014 <span title="0.04258s from github-fe117-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="/site/terms">Terms</a></li>
        <li><a href="/site/privacy">Privacy</a></li>
        <li><a href="/security">Security</a></li>
        <li><a href="/contact">Contact</a></li>
    </ul>
  </div><!-- /.site-footer -->
</div><!-- /.container -->


    <div class="fullscreen-overlay js-fullscreen-overlay" id="fullscreen_overlay">
  <div class="fullscreen-container js-suggester-container">
    <div class="textarea-wrap">
      <textarea name="fullscreen-contents" id="fullscreen-contents" class="fullscreen-contents js-fullscreen-contents js-suggester-field" placeholder=""></textarea>
    </div>
  </div>
  <div class="fullscreen-sidebar">
    <a href="#" class="exit-fullscreen js-exit-fullscreen tooltipped tooltipped-w" aria-label="Exit Zen Mode">
      <span class="mega-octicon octicon-screen-normal"></span>
    </a>
    <a href="#" class="theme-switcher js-theme-switcher tooltipped tooltipped-w"
      aria-label="Switch themes">
      <span class="octicon octicon-color-mode"></span>
    </a>
  </div>
</div>



    <div id="ajax-error-message" class="flash flash-error">
      <span class="octicon octicon-alert"></span>
      <a href="#" class="octicon octicon-x flash-close js-ajax-error-dismiss" aria-label="Dismiss error"></a>
      Something went wrong with that request. Please try again.
    </div>


      <script crossorigin="anonymous" src="https://assets-cdn.github.com/assets/frameworks-fe1dc1d9b25d6a0cda010576a3c4be01906c9fd2ac07462042e5fceda326d50c.js" type="text/javascript"></script>
      <script async="async" crossorigin="anonymous" src="https://assets-cdn.github.com/assets/github-1b193f099fcf1a4bd66e7ca7d03d0653f3e3a8d4d7846ef04476d17e0fecc695.js" type="text/javascript"></script>
      
      
  </body>
</html>

