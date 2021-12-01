terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.56.0"
    }
  }
   backend "s3" {
     bucket = "pgr301-ruda003-terraform"
     region = "eu-west-1"
   }
#  backend "remote" {
#    organization = "ruda003"
#    workspaces {
#      name = "gh-actions"
#    }
#  }
}
